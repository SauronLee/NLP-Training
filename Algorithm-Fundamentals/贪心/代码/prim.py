node_names = {0: 'A',
              1: 'B',
              2: 'C',
              3: 'D',
              4: 'E',
              5: 'F',
              6: 'G'}
nodes = [0, 1, 2, 3, 4, 5, 6]
graph =[[-1] * 7 for i in range(7)]
graph[0][1] = graph[1][0] = 7 # AB
graph[0][3] = graph[3][0] = 5 # AD
graph[1][2] = graph[2][1] = 8 # BC
graph[1][3] = graph[3][1] = 9 # BD
graph[1][4] = graph[4][1] = 7 # BE
graph[2][4] = graph[4][2] = 5 # CE
graph[3][4] = graph[4][3] = 15 # DE
graph[3][5] = graph[5][3] = 6 # DF
graph[4][5] = graph[5][4] = 8 # EF
graph[4][6] = graph[6][4] = 9 # EG
graph[5][6] = graph[6][5] = 11 # FG
vnew = [] # 待添加的节点
enew = [] # 待添加的边

vnew.append(nodes.pop())
while nodes:
    min_edge = 10000
    edge = []
    for i in vnew:
        for j in nodes:
            if graph[i][j] != -1:
                if graph[i][j] < min_edge:
                    min_edge = graph[i][j]
                    edge = [i, j]
    nodes.remove(edge[1])
    vnew.append(edge[1])
    enew.append(edge)
for edge in enew:
    i, j = edge
    print('%s-%s: %d' % (node_names[i], node_names[j], graph[i][j]))
                        