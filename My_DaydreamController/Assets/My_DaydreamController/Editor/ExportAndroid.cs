/* 
	 pengxinkai 2017-03-31
*/

using UnityEditor;
using UnityEngine;
using System.Collections;
using System.IO;

public class ExportAndroidScript : Editor
{

    [MenuItem("Tools/Export Android Project")]
    static public void BuildAssetBundles()
    {
        string runningPlatform = Application.platform.ToString();
//        Debug.Log("Build On " + runningPlatform);
        string rootDir = Path.GetFullPath(Path.Combine(Application.dataPath, "../../"));
      //  Debug.Log("root dir: " + rootDir);

        string outputDir = Path.Combine(rootDir, "tmp/android_project");
        string result;                                     //这里是unity项目中 scenes的位置
		result = BuildPipeline.BuildPlayer(new string[] { "Assets/My_DaydreamController/my_controller.unity" }, outputDir, BuildTarget.Android, BuildOptions.AcceptExternalModificationsToPlayer);
        //result = "";
        if (result.CompareTo("") == 0)
        {
            Debug.Log("Build Project OK!");                          //这里的路径是指tools导出后tmp下工程名下的assets文件夹
			string assetsDir = Path.GetFullPath(Path.Combine(outputDir, "My_DaydreamController/assets/bin/"));
            Debug.Log(assetsDir);

            // bin dir should empty
            // rm android/bin/*
            string destDir;
            if (runningPlatform.CompareTo("OSXEditor") == 0)
			{                                                      //这里的目录 是指tootl导出工程后 AndroidStudio要导入工程后选择要创建的路径(建议创建AS工程时最好建在tmp同一个根目录下)
				destDir = Path.GetFullPath(Path.Combine(rootDir, "Android/my_DaydreamController/src/main/assets/bin/"));

            }
            else
            {
				destDir = Path.GetFullPath(Path.Combine(rootDir, "Android/my_DaydreamController/src/main/assets/bin/"));
            }

            //assetsDir = assetsDir.Replace ("\\", "/");
            //destDir = destDir.Replace ("\\", "/"
            //if (File.Exists (destDir) == true) {
            bool success = FileUtil.DeleteFileOrDirectory(destDir);
            if (success == false)
            {
				//第一次导出会删除失败 因为要删除的文件夹还不存在
                Debug.Log("delete dest folder failed");
                return;
            }
            //}
			//复制 assets文件夹到 Androidstudio工程中
            FileUtil.CopyFileOrDirectory(assetsDir, destDir);
        }
        else
        {
            Debug.Log("Build Android Project Failed: " + result);
        }
    }
}