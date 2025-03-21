'''
3x3采样小图片. 3x3填充大图片
'''
from PIL import Image
import numpy as np

large_file_path = "input/bulaien1080x1080.jpg"
small_file_path = "input/shenli.jpg"

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
    # 3x3矩阵求(r,g,b)平均值
    # r = (mat[i-1][j-1] + mat[i-1][j] + mat[i-1][j+1] +
    #      mat[i][j-1] + mat[i][j] + mat[i][j+1] +
    #      mat[i+1][j-1] + mat[i+1][j] + mat[i+1][j+1]) // 9
    # return r
    return mat[i][j]

# 缩放small_matrix
for i in range(1, ss[0] - 1, 2):
    row = []
    for j in range(1, ss[1] - 1, 2):
        row.append(smaple(small_matrix, i, j))
    small_scale_matrix.append(row)

small_scale_matrix = np.array(small_scale_matrix).astype(np.uint8)

Image.fromarray(small_scale_matrix).save("./image2/scale.jpg")

ss = small_scale_matrix.shape

# 统计到small_matrix的第几个元素
cnt = 0
def pos_in_matrix():
    return (cnt // ss[1], cnt % ss[1])

for i in range(1, ls[0] - 1, 2):
    for j in range(1, ls[1] - 1, 2):
        x, y = pos_in_matrix()
        if cnt >= ss[0] * ss[1]:
            break
        large_matrix[i][j] = small_scale_matrix[x][y]
        cnt += 1

# 将large_matrix写入文件
Image.fromarray(large_matrix).save("./image2/result-" + str(cnt) + ".jpg")