'''
放大大图
'''
import cv2
from PIL import Image
import numpy as np

N = 3
if N & 1 != 1:
    print("N只能输入基数")
    exit()

large_scale = 3

large_file_path = "input/bulaien1080x1080.png"
small_file_path = "input/shenli.png"

large_matrix = np.array(Image.open(large_file_path))
small_matrix = np.array(Image.open(small_file_path))

# 比较大小
if large_matrix.shape < small_matrix.shape:
    print("小图无法覆盖大图")
    exit()

# 放大大图
large_matrix = cv2.resize(large_matrix, (large_matrix.shape[0] * large_scale, large_matrix.shape[1] * large_scale), interpolation=cv2.INTER_CUBIC)

ls = large_matrix.shape
ss = small_matrix.shape

small_scale_matrix = []

def smaple(mat, i, j):
    return mat[i][j]

# 缩放small_matrix
step = N // 2
for i in range(step, ss[0], step * 2):
    row = []
    for j in range(step, ss[1], step * 2):
        row.append(smaple(small_matrix, i, j))
    small_scale_matrix.append(row)

small_scale_matrix = np.array(small_scale_matrix).astype(np.uint8)

Image.fromarray(small_scale_matrix).save("./image4/scale.png")

ss = small_scale_matrix.shape

# 统计到small_matrix的第几个元素
cnt = 0
def pos_in_matrix():
    return (cnt // ss[1], cnt % ss[1])

for i in range(step, ls[0], step * 2 * large_scale):
    if cnt >= ss[0] * ss[1]:
        break
    for j in range(step, ls[1], step * 2 * large_scale):
        x, y = pos_in_matrix()
        if cnt >= ss[0] * ss[1]:
            break
        large_matrix[i][j] = small_scale_matrix[x][y]
        cnt += 1

# 将large_matrix写入文件
Image.fromarray(large_matrix).save("./image4/result-" + str(cnt) + "x" + str(ss[0]) + ".png")
