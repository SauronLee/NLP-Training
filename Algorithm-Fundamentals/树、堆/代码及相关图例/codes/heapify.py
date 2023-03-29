class Solution:
    # @param A: Given an integer array
    # @return: void
    def heapify(self, A):
        # write your code here
        for i in xrange((len(A) - 1) / 2, -1, -1):
            while i < len(A):
                left, right = i * 2 + 1, i * 2 + 2
                min_pos = i
                if (left < len(A)) and (A[left] < A[min_pos]):
                    min_pos = left
                if (right < len(A)) and (A[right] < A[min_pos]):
                    min_pos = right
                if min_pos != i:
                    A[i], A[min_pos] = A[min_pos], A[i]
                    i = min_pos
                else:
                    break

# http://lintcode.com/zh-cn/problem/heapify/