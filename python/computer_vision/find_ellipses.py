import numpy as np
import sys
from sklearn.metrics.pairwise import euclidean_distances
from skimage.transform import hough_ellipse
from skimage.draw import ellipse_perimeter, ellipse
import math
import cv2


def canny_threshold(image, low_threshold=100, ratio=3, kernel_size=3):
    """Canny Threshold"""

    # Reduce noise with a kernel 3x3
    image = cv2.blur(image, (3, 3))

    # Canny detector
    image = cv2.Canny(image, low_threshold, low_threshold * ratio, kernel_size)

    return image


def corners(contour):
    """Corners"""

    most_left = [[sys.maxsize, 0]]
    most_right = [[0, 0]]
    most_top = [[0, sys.maxsize]]
    most_bottom = [[0, 0]]

    for point in contour:
        # If most left
        if point[0][0] <= most_left[0][0]:
            most_left = point
        # If most right
        if point[0][0] >= most_right[0][0]:
            most_right = point
        # If most top
        if point[0][1] <= most_top[0][1]:
            most_top = point
        # If most bottom
        if point[0][1] >= most_bottom[0][1]:
            most_bottom = point

    return np.array(most_left[0]), np.array(most_right[0]), np.array(most_top[0]), np.array(most_bottom[0])


def get_axes(contour):
    """Get Axes"""

    most_left, most_right, most_top, most_bottom = corners(contour)

    major_axis = euclidean_distances(most_left[0], most_right[0])[0]
    minor_axis = euclidean_distances(most_top[1], most_bottom[1])[0]

    return major_axis, minor_axis


def is_good_distance(contour, min_major_axis, min_minor_axis):
    """Is Good Distance"""

    major_axis, minor_axis = get_axes(contour)

    if major_axis >= min_major_axis and minor_axis >= min_minor_axis:
        return True
    else:
        return False


def make_ellipse_image(center_y, center_x, a, b):
    """Make Ellipse Image"""

    image = np.zeros((center_y * 2, center_x * 2), dtype=np.uint8)
    cy, cx = ellipse(center_y, center_x, a, b)
    image[cy, cx] = 255

    return image


def main(argv):
    if len(argv) == 0:
        print("Please specify input and output image files.")

    if len(argv) == 1:
        print("Please specify an output image file.")

    input_image_path = argv[0]
    output_image_path = argv[1]

    # Used for shape matching
    ellipse_image = make_ellipse_image(50, 100, 40, 80)
    threshold_ellipse = canny_threshold(ellipse_image, 40, 2)

    ellipse_image_contours, contours, hierarchy = cv2.findContours(threshold_ellipse, cv2.RETR_TREE,
                                                                   cv2.CHAIN_APPROX_SIMPLE)
    ellipse_contour = contours[1]

    # Input image
    image = cv2.imread(input_image_path)
    height, width = image.shape[:2]

    threshold_image = canny_threshold(image, 40, 2)
    kernel = np.ones((5, 5), np.uint8)
    threshold_image = cv2.morphologyEx(threshold_image, cv2.MORPH_DILATE, kernel)
    image_contours, contours, hierarchy = cv2.findContours(threshold_image, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    for contour in contours:
        major_axis, minor_axis = get_axes(contour)

        # If contour too small
        if not is_good_distance(contour, 10, 10):
            continue

        # If shape too large
        if major_axis > width * 0.70 or minor_axis > height * 0.70:
            continue

        # Match the current contour with ellipse shape contour
        ret = cv2.matchShapes(ellipse_contour, contour, 2, 0)

        # Key parameter
        if ret > 0.75:
            continue

        # Draw an ellipse in the image
        most_left, most_right, most_top, most_bottom = corners(contour)

        center_y = int((most_top[1] + most_bottom[1]) / 2)
        center_x = int((most_right[0] + most_left[0]) / 2)

        cy, cx = ellipse_perimeter(center_y, center_x, int(major_axis / 2), int(minor_axis / 2), math.radians(90))
        image[cy, cx] = (0, 255, 0)

    cv2.imwrite(output_image_path, image)


if __name__ == "__main__":
    main(sys.argv[1:])
