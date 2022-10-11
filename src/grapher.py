import math
import os
# imports
import matplotlib.pyplot as plt
from matplotlib.backends.backend_pdf import PdfPages
import numpy as np
from matplotlib import rc

# Set the styles for the graphs
rc('font', **{'family': 'sans-serif', 'sans-serif': ['Microsoft Tai Le'], 'size': 25})

rank_suit_list = [1, 2, 4, 13, 26, 52]
pile_list = [2, 4, 5, 8, 9, 10, 15, 20, 22, 25, 28, 30, 32, 35, 38, 40, 42, 45, 50, 52]

deck_list = [5, 14, 30, 52, 60, 85, 120, 132, 175, 750, 3000, 6900, 12200, 15525, 19000, 43125, 76500, 120000, 172500,
             307000, 480000]

seed_list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

directory = ".." + os.path.sep + "results"

part_list = ["part2", "part3", "recursive-part2"]


# graphs the pathology of the list being completely sorted for each list size
def graph(sub_dir, x_axis, title, list, tupleIndex):
    filename = ""
    for p in part_list:
        fig = plt.figure(figsize=(30, 15), dpi=80)
        x = []
        y = []
        focus = "time" if tupleIndex == 1 else "space" if tupleIndex == 2 else "solution"
        filename = ".." + os.path.sep + "graphs" + os.path.sep + sub_dir + "-" + focus + "-" + p + ".pdf"
        pp = PdfPages(filename)
        for i in list:
            path = directory + os.path.sep + p + os.path.sep + sub_dir + os.path.sep + str(i)
            try:
                files = os.listdir(path)
            except FileNotFoundError:
                continue
            results = []
            for file in files:
                new_file = open(path + os.path.sep + file)
                data = new_file.readline().split(":")
                if tupleIndex != 3:
                    results.append(float(data[tupleIndex]))
                else:
                    results.append(data[tupleIndex])

            if tupleIndex == 3:
                trueCount = 0
                for r in results:
                    if r == "true":
                        trueCount += 1
                y.append(round(trueCount / len(results) * 100, 2))
            else:
                y.append(np.mean(results))
            x.append(i)
        color = ""
        label = ""
        if p == "part2":
            color = "g"
            label = "List-based Black Hole Solver"
        else:
            if p == "part3":
                color = "r"
                label = "List-based Worm Hole Solver"
            else:
                if p == "recursive-part2":
                    color = "b"
                    label = "Recursive-based Black Hole Solver"

        plt.plot(x, y, linestyle="--", label=label, marker="o", color=color, markerfacecolor="b", markersize=10)


        plt.xlabel(x_axis)
        plt.legend(loc="upper left")
        if tupleIndex == 1:
            plt.ylabel("Time taken to search\n(Milliseconds)")
        if tupleIndex == 2:
            plt.ylabel("Number of nodes visited during execution")
        if tupleIndex == 3:
            plt.ylabel("Percentage of puzzles solved")
        plt.title(title)
        fig.savefig(pp, format='pdf')
        print("Saving " + filename)
        plt.close(fig)

        pp.close()


graph("pile", "Number of Piles", "Effect of Number of Piles on Execution Time", pile_list, 1)

graph("decksize", "Number of Cards", "Effect of Increasing Deck Size on Execution Time", deck_list, 1)

graph("suit", "Number of Suits", "Effect of Number of Suits on Execution Time", rank_suit_list, 1)

graph("rank", "Number of Ranks", "Effect of Number of Ranks on Execution Time", rank_suit_list, 1)

graph("pile", "Number of Piles", "Effect of Number of Piles on 'Winnability'", pile_list, 3)

graph("suit", "Number of Suits", "Effect of Number of Suits on 'Winnability'", rank_suit_list, 3)

graph("pile", "Number of Piles", "Effect of Number of Piles on Node Count", pile_list, 2)

graph("rank", "Number of Ranks", "Effect of Number of Ranks on 'Winnability'", rank_suit_list, 3)

graph("decksize", "Number of Cards", "Effect of Number of Cards on 'Winnability'", deck_list, 3)

graph("seed", "Seed Generator", "Effect of Seed on Execution Time", seed_list, 1)