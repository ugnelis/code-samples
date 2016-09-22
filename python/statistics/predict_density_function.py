"""For a given data set X, predict its density function. Using the found
density function generate new random data set Y and check the hypothesis
that X and Y distributions are identical. A sample data set is
added in the file datasets.zip."""

from scipy.stats import gaussian_kde
import numpy as np
import matplotlib.pyplot as plt
import math
from scipy import stats


def kde(x, x_grid, bandwidth=0.2):
    # kde = gaussian_kde(x, bw_method=bandwidth / x.std(ddof=1))
    kde = gaussian_kde(x, bw_method=bandwidth / x.std(ddof=1))
    return kde.evaluate(x_grid)


def main():
    data = np.loadtxt("dataset.txt")
    n = data.shape[0] / 10
    min_x = np.min(data)
    max_x = np.max(data)

    hist, bins = np.histogram(data, bins=n, range=(min_x, max_x), density=True)
    center = (bins[:-1] + bins[1:]) / 2

    start = math.floor(min(data))
    end = math.ceil(max(data))
    x_grid = np.linspace(start, end, n)

    # Plot the three kernel density estimates
    fig, ax = plt.subplots(1, 3, sharey=True, figsize=(15, 4))

    pdf = kde(data, x_grid, bandwidth=0.1)

    ax[0].set_title("Default")
    ax[0].plot(center, hist, color='blue', linewidth=2)

    ax[1].set_title("KDE")
    ax[1].plot(x_grid, pdf, color='blue', linewidth=2)

    ax[2].set_title("Merged")
    ax[2].plot(center, hist, color='blue', alpha=0.5, linewidth=2)
    ax[2].plot(x_grid, pdf, color='red', linewidth=2)

    # Check hypothesis
    # https://en.wikipedia.org/wiki/Kolmogorov%E2%80%93Smirnov_test#Two-sample_Kolmogorov.E2.80.93Smirnov_test
    cumulative_default = np.cumsum(hist)
    cumulative_kde = np.cumsum(pdf)
    D = np.max(np.absolute(cumulative_default - cumulative_kde))

    fig, ax = plt.subplots(1, 1, sharey=True, figsize=(5, 4))
    ax.set_title("Cumulative Distribution Function")
    ax.plot(cumulative_default, color='blue', alpha=0.5, linewidth=2)
    ax.plot(cumulative_kde, color='red', linewidth=2)
    plt.show()

    D, p = stats.ks_2samp(cumulative_default, cumulative_kde)

    # Checks whether the two data samples come from the same distribution
    significance_levels = [1.22, 1.36, 1.48, 1.63, 1.73, 1.95]
    for c_a in significance_levels:
        if D > c_a * np.sqrt((n + n) / (n * n)):
            print("Null hypothesis, if c(a) =", c_a)
        else:
            print("Same distribution, if c(a) =", c_a)


if __name__ == "__main__":
    main()
