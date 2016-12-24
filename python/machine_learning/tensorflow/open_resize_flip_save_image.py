import tensorflow as tf
import os
from PIL import Image


def modify_image(image):
    resized = tf.image.resize_images(image, 180, 180, 1)
    resized.set_shape([180, 180, 3])
    flipped_images = tf.image.flip_up_down(resized)
    return flipped_images


def read_image(filename_queue):
    reader = tf.WholeFileReader()
    key, value = reader.read(filename_queue)
    image = tf.image.decode_jpeg(value)
    return image


def inputs():
    file_names = ['1.jpg', '2.jpg']  # List of images
    file_name_queue = tf.train.string_input_producer(file_names)
    read_input = read_image(file_name_queue)
    reshaped_image = modify_image(read_input)
    return reshaped_image


def main():
    cur_dir = os.getcwd()
    print("current directory:", cur_dir)

    with tf.Graph().as_default():
        image = inputs()
        init = tf.initialize_all_variables()
        sess = tf.Session()
        sess.run(init)
        tf.train.start_queue_runners(sess=sess)
        for i in range(2):
            img = sess.run(image)
            img = Image.fromarray(img, "RGB")
            img.save(os.path.join(cur_dir, "foo" + str(i) + ".jpeg"))


if __name__ == '__main__':
    main()
