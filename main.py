#!/usr/bin/python3
# -*- coding: utf-8 -*-
from __future__ import division
import sys
import os
import matplotlib.pyplot as plt
import networkx as nx
from utilities import *
from plots import *

dir_name = "Outputs/"
files = os.listdir(dir_name)
for fn in files:
    Data1 = open(dir_name + fn, "r")
    Data2 = open(dir_name + fn, "r")
    DiGraphtype = nx.DiGraph()
    DG = nx.parse_edgelist(Data1,
                           delimiter=';',
                           create_using=DiGraphtype,
                           nodetype=int)
    Graphtype = nx.Graph()
    G = nx.parse_edgelist(Data2,
                          delimiter=';',
                          create_using=Graphtype,
                          nodetype=int)

    m, n, k = parse_fields(fn)
    stats(DG, m, n, k)
    #save_graph(DG, m, n, k)
    print(fn)
    #save_degree_rank_plot(G, m, n, k)
    #save_in_degree_rank_plot(DG, m, n, k)
    #save_out_degree_rank_plot(DG, m, n, k)
    #save_in_degree_graph(G, m, n, k)
    #save_put_degree_graph(G, m, n, k)
    #save_in_plus_out_degree_hist(G, m, n, k)
"""m = 16
n = 1000
k = 5
fn = "output" + str(m) + "x" + str(n) + "x" + str(k) + ".csv"

Data1 = open(dir_name + fn, "r")
Data2 = open(dir_name + fn, "r")
DiGraphtype = nx.DiGraph()
DG = nx.parse_edgelist(Data1,
                       delimiter=';',
                       create_using=DiGraphtype,
                       nodetype=int)
Graphtype = nx.Graph()
G = nx.parse_edgelist(Data2,
                      delimiter=';',
                      create_using=Graphtype,
                      nodetype=int)

#save_graph(DG, m, n, k)
#save_degree_rank_plot(G, m, n, k)
#save_in_degree_rank_plot(DG, m, n, k)
#save_out_degree_rank_plot(DG, m, n, k)
save_in_degree_graph(DG, m, n, k)
save_out_degree_graph(DG, m, n, k)
save_in_plus_out_degree_hist(DG, m, n, k)"""
