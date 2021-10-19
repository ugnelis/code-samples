#!/usr/bin/env python

from __future__ import print_function
import numpy as np
import tensorflow as tf
from six.moves import cPickle as pickle
from six.moves import range
from scipy import ndimage
import os

pickle_file = 'notMNIST.pickle'
image_size = 28
num_labels = 10
pixel_depth = 255.0  # Number of levels per pixel.
L2_weight = 5e-4


def get_image_array(image_path):
    dataset = np.ndarray(shape=(image_size, image_size), dtype=np.float32)
    try:
        image_data = (ndimage.imread(image_patmatplotlibh).astype(float) - pixel_depth / 2) / pixel_depth
        if image_data.shape != (image_size, image_size):
            raise Exception('Unexpected image shape: %s' % str(image_data.shape))
        dataset[:, :] = image_data
    except IOError as e:
        print('Could not read:', image_path, ':', e, '- it\'s ok, skipping.')

    return dataset


def reformat(dataset, labels):
    dataset = dataset.reshape((-1, image_size * image_size)).astype(np.float32)
    # Map 0 to [1.0, 0.0, 0.0 ...], 1 to [0.0, 1.0, 0.0 ...]
    labels = (np.arange(num_labels) == labels[:, None]).astype(np.float32)
    return dataset, labels


def accuracy(predictions, labels):
    return (100.0 * np.sum(np.argmax(predictions, 1) == np.argmax(labels, 1))
            / predictions.shape[0])


def open_data(pickle_file):
    with open(pickle_file, 'rb') as f:
        save = pickle.load(f)
        train_dataset = save['train_dataset']
        train_labels = save['train_labels']
        valid_dataset = save['valid_dataset']
        valid_labels = save['valid_labels']
        test_dataset = save['test_dataset']
        test_labels = save['test_labels']
        del save  # hint to help gc free up memory
        print('Training set', train_dataset.shape, train_labels.shape)
        print('Validation set', valid_dataset.shape, valid_labels.shape)
        print('Test set', test_dataset.shape, test_labels.shape)

    return train_dataset, train_labels, valid_dataset, valid_labels, test_dataset, test_labels


# Create model
def multilayer_perceptron(x, weights, biases):
    # Hidden layer with RELU activation
    layer_1 = tf.add(tf.matmul(x, weights['h1']), biases['b1'])
    layer_1 = tf.nn.relu(layer_1)

    layer_2 = tf.add(tf.matmul(layer_1, weights['h2']), biases['b2'])
    layer_2 = tf.nn.relu(layer_2)

    # Output layer with linear activation
    out_layer = tf.matmul(layer_2, weights['out']) + biases['out']
    return out_layer


# Create model
def multilayer_perceptron_with_dropout(x, weights, biases, dropout):
    # Hidden layer with RELU activation
    layer_1 = tf.add(tf.matmul(x, weights['h1']), biases['b1'])
    layer_1 = tf.nn.dropout(tf.nn.relu(layer_1), dropout)

    layer_2 = tf.add(tf.matmul(layer_1, weights['h2']), biases['b2'])
    layer_2 = tf.nn.dropout(tf.nn.relu(layer_2), dropout)

    # Output layer with linear activation
    out_layer = tf.matmul(layer_2, weights['out']) + biases['out']
    return out_layer


def get_result(image_path, labels, sess, weights, biases):
    dataset = get_image_array(image_path)
    dataset = dataset.reshape((-1, image_size * image_size)).astype(np.float32)
    tf_dataset = tf.constant(dataset)

    y = tf.nn.softmax(multilayer_perceptron(tf_dataset, weights, biases))

    predictions = sess.run(y, feed_dict={tf_dataset: dataset})

    top_k = predictions[0].argsort()[-len(predictions[0]):][::-1]

    for node_id in top_k:
        human_string = labels[node_id]
        score = predictions[0][node_id]
        print('%s (score = %.5f)' % (human_string, score))


def main(argv):
    train_dataset, train_labels, valid_dataset, valid_labels, test_dataset, test_labels = open_data(pickle_file)

    train_dataset, train_labels = reformat(train_dataset, train_labels)
    valid_dataset, valid_labels = reformat(valid_dataset, valid_labels)
    test_dataset, test_labels = reformat(test_dataset, test_labels)
    print('Training set', train_dataset.shape, train_labels.shape)
    print('Validation set', valid_dataset.shape, valid_labels.shape)
    print('Test set', test_dataset.shape, test_labels.shape)

    batch_size = 128

    # Network Parameters
    n_hidden_1 = 1024  # 1st layer number of features
    n_hidden_2 = 1024  # 2st layer number of features
    n_input = image_size * image_size

    graph = tf.Graph()
    with graph.as_default():
        # Input data. For the training data, we use a placeholder that will be fed
        # at run time with a training minibatch.
        tf_train_dataset = tf.placeholder(tf.float32, shape=(batch_size, image_size * image_size))
        tf_train_labels = tf.placeholder(tf.float32, shape=(batch_size, num_labels))
        tf_valid_dataset = tf.constant(valid_dataset)
        tf_test_dataset = tf.constant(test_dataset)

        # Variables.
        weights = {
            'h1': tf.Variable(tf.truncated_normal([n_input, n_hidden_1])),
            'h2': tf.Variable(tf.truncated_normal([n_hidden_1, n_hidden_2])),
            'out': tf.Variable(tf.truncated_normal([n_hidden_2, num_labels]))
        }
        biases = {
            'b1': tf.Variable(tf.zeros([n_hidden_1])),
            'b2': tf.Variable(tf.zeros([n_hidden_2])),
            'out': tf.Variable(tf.zeros([num_labels]))
        }

        # Dropout
        dropout = tf.placeholder(tf.float32)

        train_prediction = multilayer_perceptron_with_dropout(tf_train_dataset, weights, biases, 0.8)

        # L2 regularization
        l2_regularization = tf.nn.l2_loss(weights['h1']) + tf.nn.l2_loss(weights['h2']) + tf.nn.l2_loss(weights['out'])

        loss = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(train_prediction, tf_train_labels))
        total_loss = loss + L2_weight * l2_regularization

        # Learning rate
        global_step = tf.Variable(0)  # count the number of steps taken.
        learning_rate = tf.train.exponential_decay(learning_rate=0.0001,
                                                   global_step=global_step,
                                                   decay_steps=1200,
                                                   decay_rate=0.96,
                                                   staircase=True)

        # Optimizer.
        optimizer = tf.train.GradientDescentOptimizer(learning_rate=learning_rate) \
            .minimize(total_loss, global_step=global_step)

        valid_prediction = tf.nn.softmax(multilayer_perceptron(tf_valid_dataset, weights, biases))
        test_prediction = tf.nn.softmax(multilayer_perceptron(tf_test_dataset, weights, biases))

    num_steps = 5001

    with tf.Session(graph=graph) as sess:
        tf.initialize_all_variables().run()
        print("Initialized")
        for step in range(num_steps):
            # Pick an offset within the training daectified linear ta, which has been randomized.
            # Note: we could use better randomization across epochs.
            offset = (step * batch_size) % (train_labels.shape[0] - batch_size)
            # Generate a minibatch.
            batch_data = train_dataset[offset:(offset + batch_size), :]
            batch_labels = train_labels[offset:(offset + batch_size), :]
            # Prepare a dictionary telling the session where to feed the minibatch.
            # The key of the dictionary is the placeholder node of the graph to be fed,
            # and the value is the numpy array to feed to it.
            feed_dict = {tf_train_dataset: batch_data, tf_train_labels: batch_labels}
            _, l, predictions = sess.run([optimizer, total_loss, train_prediction], feed_dict=feed_dict)

            if (step % 500 == 0):
                print("Minibatch loss at step %d: %f" % (step, l))
                print("Minibatch accuracy: %.1f%%" % accuracy(predictions, batch_labels))
                print("Validation accuracy: %.1f%%" % accuracy(valid_prediction.eval(), valid_labels))

        print("Test accuracy: %.1f%%" % accuracy(test_prediction.eval(), test_labels))  # Get result

        image_path = argv[1]
        print(image_path)
        labels = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J']
        get_result(image_path, labels, sess, weights, biases)


if __name__ == '__main__':
    tf.app.run()
