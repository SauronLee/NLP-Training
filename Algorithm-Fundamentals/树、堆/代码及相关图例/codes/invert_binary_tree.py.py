# coding: utf-8

class Solution:
    # @param root: a TreeNode, the root of the binary tree
    # @return: nothing
    def invertBinaryTree(self, root):
        # write your code here
        if root is None:
            return
        root.left, root.right = root.right, root.left
        self.invertBinaryTree(root.left)
        self.invertBinaryTree(root.right)
		
    def loopVersion(self, root):
        # write your code here
        if root is None:
            return
        stack = [root]
        while stack:
            node = stack.pop()
            self.swap(node)
            if node.left:
                stack.append(node.left)
            if node.right:
                stack.append(node.right)

    def swap(self, node):
        node.left, node.right = node.right, node.left
		
# http://lintcode.com/zh-cn/problem/invert-binary-tree/