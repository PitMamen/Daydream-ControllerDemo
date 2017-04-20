using UnityEngine;
using System.Collections;
using System;


public class My_Controller : MonoBehaviour {

	AndroidJavaObject objectStore ;
	AndroidJavaObject providerObject;
	private GameObject mCube;
	
	private float[] floatDatas;
	private float f1;
	private float f2;
	private float f3;

	// Use this for initialization
	void Start () {
		Debug.Log("Provider  start!!");
		Debug.Log("==================");

		mCube = GameObject.Find ("Cube");
		if(mCube==null){
			Debug.Log ("Cube not Find!!!");
		}

		objectStore = new AndroidJavaObject("com.Szmygt.app.vr.utils.ObjectStore");
		providerObject = objectStore.CallStatic<AndroidJavaObject>("get","UnityPlayerActivity");
		Debug.Log ("object=="+providerObject);

		floatDatas = providerObject.Call<float[]> ("getfloatArray");
//		 f1 = floatDatas [0];
//		Debug.Log ("f1==："+f1);
//		 f2 = floatDatas [1];
//		Debug.Log ("f2==："+f2);
//		 f3 = floatDatas [2];
//		Debug.Log ("f3==："+f3);




	}


	void Update () {
		floatDatas = providerObject.Call<float[]> ("getfloatArray");
		if(Time.frameCount%30==0)
		{
			if(floatDatas!=null)
			{
				f1 = floatDatas [0];
			//	Debug.Log ("f1==："+f1);
				f2 = floatDatas [1];
			//	Debug.Log ("f2==："+f2);
				f3 = floatDatas [2];
			//	Debug.Log ("f3==："+f3);
			}
		}
				print ("f1=="+f1+"  "+"f1=="+f2+"   "+"f3=="+f3);
		                         //欧拉
		mCube.transform.rotation = Quaternion.Euler (f1,f2,f3);
	//	Debug.Log (mCube.transform.rotation.x+"  "+transform.rotation.y+"   "+transform.rotation.z);
//		mCube.transform.Rotate(f1,f2,f3,Space.Self);
	}
}
