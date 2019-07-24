#!/usr/bin/python3
# -*- coding: utf-8 -*-

import sys
import os
import matplotlib.pyplot as plt
import networkx as nx


def parse_fields(file_name):
    fields = file_name.split("x")
    k = fields[2].split(".")[0]
    m = fields[0].split("output")[1]
    n = fields[1]
    return m, n, k


def stats(G, m, n, k):
    in_d = 0
    out_d = 0
    nodes = list(G.nodes)
    for nd in nodes:
        in_d += G.in_degree(nd)
        out_d += G.out_degree(nd)
    in_d = in_d / len(nodes)
    out_d = out_d / len(nodes)
    largest = max(nx.strongly_connected_components(G), key=len)
    diam = nx.diameter(G.subgraph(largest))
    ecc = nx.eccentricity(G.subgraph(largest))
    avg_ecc = 0
    for e, nd in ecc:
        avg_ecc += e
    avg_ecc = avg_ecc / len(ecc)
    clu = clustering(G)
    avg_clu = 0
    for c, nd in clu:
        avg_clu += c
    avg_clu = avg_cluecc / len(clu)

    line = str(k) + " " + str(m) + " " + str(n) + " " + str(
        nx.number_of_nodes(G)) + " " + str(nx.number_of_edges(G)) + " " + str(
            nx.density(G)) + " " + str(in_d) + " " + str(out_d) + " " + str(
                diam) + " " + str(avg_ecc) + " " + str(avg_clu) + "\n"
    with open("stats.txt", "a+") as f:
        f.write(line)
