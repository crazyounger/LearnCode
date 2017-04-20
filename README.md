# LearnCode
Android LearnCode  
平时学习用到的知识点


查看分支：git branch

创建分支：git branch <name>

切换分支：git checkout <name>

创建+切换分支：git checkout -b <name>

合并某分支到当前分支：git merge <name>

删除分支：git branch -d <name>



版本回退：

HEAD指向的版本就是当前版本，因此，Git允许我们在版本的历史之间穿梭，使用命令git reset --hard commit_id。

穿梭前，用git log可以查看提交历史，以便确定要回退到哪个版本。

要重返未来，用git reflog查看命令历史，以便确定要回到未来的哪个版本。



撤销修改：
场景1：当你改乱了工作区某个文件的内容，想直接丢弃工作区的修改时，用命令git checkout -- file。

场景2：当你不但改乱了工作区某个文件的内容，还添加到了暂存区时，想丢弃修改，分两步，第一步用命令git reset HEAD file，就回到了场景1，第二步按场景1操作。


删除文件：
 当直接在工作区删除一个文件后，使用 git status 查看：
	# On branch master
	# Changes not staged for commit:
	#   (use "git add/rm <file>..." to update what will be committed)
	#   (use "git checkout -- <file>..." to discard changes in working directory)
	#
	#       deleted:    test.txt
	#
	no changes added to commit (use "git add" and/or "git commit -a")
	
	
 如果确实要删除，使用  git rm <file>  git commit -m 'rm file'
 如果手误删除， 使用 git checkout -- <file> 可以恢复删除的文件
 


合并分支：
 可以直接使用fast forward 方式： git merge <branch-name>
 也可以使用非fast forward 方式： git merge --no-ff -m 'merge with no-ff' <branch-name> 此种方式生成一个新的节点

查看历史分支：
 git log --graph --pretty=oneline --abbrev-commit
 
 

分支删除
 git branch -d <branch-name>
 如果有未合并的分支，当删除除，提示未合并，如果强制删除，使用命令：
 git branch -D <branch-name>
 
如果在开发时候，还未提交，这时需要修复bug，可以使用
 git stash  //把工作区内容暂时缓存，然后可以建立新的分支进行修改bug
 git checkout -b bug-branch
 修改完成后，合并到对应分支上：
  git merge bug-branch
 合并完后，删除bug分支
 git branch -d bug-branch
 此时，可以恢复之前开发分支stash的内容
 git stash apply    git stash drop
 或者直接删除 git stash pop 
 使用 git stash list
 
 
 
 查看远程分支  git branch -a 
 
 删除远程分支和 tag
 git 1.7版本之后： 
   git push origin --delete <branch-name>
   git push origin --delete tag <tag-name>
 1.7之前：
   git push origin :<branch-name>   //推送一个空分支到远程分支，相当于删除
   删除tag：
	git tag -d <tag-name>
	git push origin :refs/tag/<tag-name>   


创建tag  git tag <tag-name>
		 git tag -a <tag-name> -m <tag message> <commit-id>
		 
查看tag git tag    详细的tag信息： git show <tag-name>
删除tag git tag -d <tag-name>
推动tag到远程： git push origin <tag-name>
    或者一次性推送所有本地tag: tag push origin --tags
	
删除远程tag: 1 git tag -d <tag-name> 删除本地tag
             2 git push origin :refs/tag/<tag-name>  删除远程

配置别名 :  git config --global alias st status
            git config --global alias unstage 'reset HEAD'
			
		 
     
  
 
 