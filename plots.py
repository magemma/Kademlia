#!/usr/bin/python3
# -*- coding: utf-8 -*-

import sys
import os
import numpy as np
import matplotlib as mpl
import matplotlib.pyplot as plt
import networkx as nx
import collections
import matplotlib.font_manager
matplotlib.font_manager.findSystemFonts(fontpaths=None, fontext='ttf')


def save_graph(G, m, n, k):
    pos = nx.circular_layout(G)
    nx.draw(G, pos, node_size=50, node_color="blue", width=0.2)
    file_name = "Graphs/graph" + str(m) + "x" + str(n) + "x" + str(k) + ".pdf"
    plt.savefig(file_name, format="PDF")
    plt.clf()


def save_degree_rank_plot(G, m, n, k):
    degree_sequence = sorted([d for g, d in G.degree()], reverse=True)
    dmax = max(degree_sequence)

    plt.loglog(degree_sequence, 'b-', marker='o')
    plt.title("Degree rank plot")
    plt.ylabel("degree")
    plt.xlabel("rank")

    # draw graph in inset
    plt.axes([0.45, 0.45, 0.45, 0.45])
    Gcc = sorted(nx.connected_component_subgraphs(G), key=len, reverse=True)[0]
    pos = nx.spring_layout(Gcc)
    plt.axis('off')
    #nx.draw_networkx_nodes(Gcc, pos, node_size=20)
    #nx.draw_networkx_edges(Gcc, pos, alpha=0.4)
    file_name = "DegRank/plot" + str(m) + "x" + str(n) + "x" + str(k) + ".pdf"
    plt.savefig(file_name, format="PDF")
    plt.clf()


def save_in_degree_rank_plot(G, m, n, k):
    degree_sequence = sorted([d for g, d in G.in_degree()], reverse=True)
    dmax = max(degree_sequence)

    plt.loglog(degree_sequence, 'b-', marker='o')
    plt.title("In degree rank plot")
    plt.ylabel("in degree")
    plt.xlabel("rank")

    # draw graph in inset
    plt.axes([0.45, 0.45, 0.45, 0.45])
    Gcc = sorted(nx.connected_component_subgraphs(G), key=len, reverse=True)[0]
    pos = nx.spring_layout(Gcc)
    plt.axis('off')
    #nx.draw_networkx_nodes(Gcc, pos, node_size=20)
    #nx.draw_networkx_edges(Gcc, pos, alpha=0.4)
    file_name = "InDegRank/plot" + str(m) + "x" + str(n) + "x" + str(
        k) + ".pdf"
    plt.savefig(file_name, format="PDF")
    plt.clf()


def save_out_degree_rank_plot(G, m, n, k):
    degree_sequence = sorted([d for g, d in G.out_degree()], reverse=True)
    dmax = max(degree_sequence)

    plt.loglog(degree_sequence, 'b-', marker='o')
    plt.title("Out degree rank plot")
    plt.ylabel("out degree")
    plt.xlabel("rank")

    # draw graph in inset
    plt.axes([0.45, 0.45, 0.45, 0.45])
    Gcc = sorted(nx.connected_component_subgraphs(G), key=len, reverse=True)[0]
    pos = nx.spring_layout(Gcc)
    plt.axis('off')
    #nx.draw_networkx_nodes(Gcc, pos, node_size=20)
    #nx.draw_networkx_edges(Gcc, pos, alpha=0.4)
    file_name = "OutDegRank/plot" + str(m) + "x" + str(n) + "x" + str(
        k) + ".pdf"
    plt.savefig(file_name, format="PDF")
    plt.clf()


def save_in_degree_graph(G, m, n, k):
    pos = nx.circular_layout(G)
    n = len(G.nodes)
    #a node is small if its outdegree is little
    #deg_list is the list of couples (node, out degree)
    deg_list = G.in_degree(G.nodes)
    #k is the list of in degrees
    s = [d for n, d in deg_list]
    #print(deg_list)
    #print(k)
    node_sizes = [i / n * 300 for i in s]
    #print(node_sizes)
    M = G.number_of_edges()
    edge_colors = range(2, M + 2)
    edge_alphas = [(5 + i) / (M + 4) for i in range(M)]

    nodes = nx.draw_networkx_nodes(G,
                                   pos,
                                   node_size=node_sizes,
                                   node_color='blue')
    edges = nx.draw_networkx_edges(G,
                                   pos,
                                   node_size=node_sizes,
                                   arrowstyle='->',
                                   arrowsize=10,
                                   edge_color=edge_colors,
                                   edge_cmap=plt.cm.Blues,
                                   width=2)
    # set alpha value for each edge
    for i in range(M):
        edges[i].set_alpha(edge_alphas[i])

    pc = mpl.collections.PatchCollection(edges, cmap=plt.cm.Blues)
    pc.set_array(edge_colors)
    plt.colorbar(pc)

    ax = plt.gca()
    ax.set_axis_off()
    file_name = "InDegGraph/plot" + str(m) + "x" + str(n) + "x" + str(
        k) + ".pdf"
    plt.savefig(file_name, format="PDF")
    plt.clf()


def save_out_degree_graph(G, m, n, k):
    pos = nx.circular_layout(G)
    n = len(G.nodes)
    #a node is small if its outdegree is little
    #deg_list is the list of couples (node, out degree)
    deg_list = G.out_degree(G.nodes)
    #k is the list of outdegrees
    s = [d for n, d in deg_list]
    #print(deg_list)
    #print(k)
    node_sizes = [i / n * 200 for i in s]
    #print(node_sizes)
    M = G.number_of_edges()
    edge_colors = range(2, M + 2)
    edge_alphas = [(5 + i) / (M + 4) for i in range(M)]

    nodes = nx.draw_networkx_nodes(G,
                                   pos,
                                   node_size=node_sizes,
                                   node_color='blue')
    edges = nx.draw_networkx_edges(G,
                                   pos,
                                   node_size=node_sizes,
                                   arrowstyle='->',
                                   arrowsize=10,
                                   edge_color=edge_colors,
                                   edge_cmap=plt.cm.Blues,
                                   width=2)
    # set alpha value for each edge
    for i in range(M):
        edges[i].set_alpha(edge_alphas[i])

    pc = mpl.collections.PatchCollection(edges, cmap=plt.cm.Blues)
    pc.set_array(edge_colors)
    plt.colorbar(pc)

    ax = plt.gca()
    ax.set_axis_off()
    file_name = "OutDegGraph/plot" + str(m) + "x" + str(n) + "x" + str(
        k) + ".pdf"
    plt.savefig(file_name, format="PDF")
    plt.clf()


def save_in_plus_out_degree_hist(G, m, n, k):
    x = nx.degree_histogram(G)
    occ = {}
    # for all elements
    for deg in x:
        if deg not in occ.keys():
            occ[deg] = 0
        else:
            occ[deg] += 1

    od = collections.OrderedDict(sorted(occ.items()))
    values = list(od.values())
    labels = list(od.keys())
    y_pos = np.arange(len(values))
    fig, ax = plt.subplots(figsize=(0.7 * len(labels), 5))
    plt.style.use('fivethirtyeight')
    plt.bar(y_pos, values, align='center', alpha=0.8, color="blue")
    plt.xticks(y_pos, labels)
    plt.rcParams['font.family'] = 'sans-serif'
    plt.rcParams['font.sans-serif'] = 'Helvetica'
    plt.rcParams['axes.linewidth'] = 0.8
    plt.rcParams['xtick.color'] = '#333F4B'
    plt.rcParams['ytick.color'] = '#333F4B'
    ax.set_xlabel('Degree', fontsize=15, fontweight='black', color='#333F4B')
    ax.set_ylabel('Frequency',
                  fontsize=15,
                  fontweight='black',
                  color='#333F4B')
    file_name = "InPlusOutDegHist/plot" + str(m) + "x" + str(n) + "x" + str(
        k) + ".pdf"
    plt.savefig(file_name, format="PDF")
    plt.clf()
