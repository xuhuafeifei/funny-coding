'''
nxn采样小图片. nxn填充大图片
'''
from PIL import Image
import numpy as np

N = 8

large_file_path = "input/bulaien1080x1080.png"
small_file_path = "input/shenli.png"

large_matrix = np.array(Image.open(large_file_path))
small_matrix = np.array(Image.open(small_file_path))

# 比较大小
if large_matrix.shape < small_matrix.shape:
    print("小图无法覆盖大图")
    exit()

ls = large_matrix.shape
ss = small_matrix.shape

# small_matrix采样(3x3)
small_scale_matrix = []
def smaple(mat, i, j):
    return mat[i][j]

# 缩放small_matrix
for i in range(1, ss[0], N):
    row = []
    for j in range(1, ss[1], N):
        row.append(smaple(small_matrix, i, j))
    small_scale_matrix.append(row)

small_scale_matrix = np.array(small_scale_matrix).astype(np.uint8)

Image.fromarray(small_scale_matrix).save("./image3/scale.png")

ss = small_scale_matrix.shape

# 统计到small_matrix的第几个元素
cnt = 0
def pos_in_matrix():
    return (cnt // ss[1], cnt % ss[1])

for i in range(1, ls[0], N):
    for j in range(1, ls[1], N):
        x, y = pos_in_matrix()
        if cnt > ss[0] * ss[1]:
            break
        large_matrix[i][j] = small_scale_matrix[x][y]
        cnt += 1

# 将large_matrix写入文件
Image.fromarray(large_matrix).save("./image3/result-" + str(cnt) + ".png")