'''
在大图中, 取3x3的矩阵, 选择中间点按照顺序填入小图像素
优点: 计算方便
缺点: 在大图/小图大小相当时, 小图只能保留很小一部分数据
'''
from PIL import Image
import numpy as np

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

# 统计到small_matrix的第几个元素
cnt = 0
def pos_in_matrix():
    return (cnt // ss[1], cnt % ss[1])

for i in range(1, ls[0], 2):
    for j in range(1, ls[1], 2):
        x, y = pos_in_matrix()
        if cnt >= ss[0] * ss[1]:
            break
        large_matrix[i][j] = small_matrix[x][y]
        cnt += 1

# 将large_matrix写入文件
Image.fromarray(large_matrix).save("./image1/result-" + str(cnt) + ".png")