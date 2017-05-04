using UnityEngine;
using System.Collections;
using System;


public class My_Controller : MonoBehaviour {

	AndroidJavaObject objectStore ;
	AndroidJavaObject providerObject;
	private GameObject mController;
	
	private int[] intDatas;
	private int f1;
	private int f2;
	private int f3;

	// Use this for initialization
	void Start () {
		Debug.Log("Provider  start!!");
		Debug.Log("==================");

		mController = GameObject.Find ("cuboid");
		if(mController==null){
			Debug.Log ("Cube not Find!!!");
		}

		objectStore = new AndroidJavaObject("com.Szmygt.app.vr.utils.ObjectStore");
		providerObject = objectStore.CallStatic<AndroidJavaObject>("get","UnityPlayerActivity");
		Debug.Log ("object=="+providerObject);

		intDatas = providerObject.Call<int[]> ("getIntData");

		Debug.Log ("intDatas=="+intDatas.Length);

//		 f1 = floatDatas [0];
//		Debug.Log ("f1==："+f1);
//		 f2 = floatDatas [1];
//		Debug.Log ("f2==："+f2);
//		 f3 = floatDatas [2];
//		Debug.Log ("f3==："+f3);




	}


	void Update () {
		intDatas = providerObject.Call<int[]> ("getIntData");
		Debug.Log ("intDatas=="+intDatas.Length);
		if(Time.frameCount%30==0)
		{
			if(intDatas.Length>0)
			{
				f1 = intDatas [0];
				Debug.Log ("f1==："+intDatas[0]);
				f2 = intDatas [1];
				Debug.Log ("f2==："+intDatas[1]);
				f3 = intDatas [2];
				Debug.Log ("f3==："+intDatas[2]);
			}
		}
//		print ("f1=="+intDatas[0]+"  "+"f2=="+intDatas[1]+"   "+"f3=="+intDatas[2]);
		                                   //欧拉
//		mController.transform.rotation = Quaternion.Euler ((float)f1/2047*180,(float)f2/2047*180,(float)f3/2047*180);
		//Use This Mothod
		//mController.transform.Rotate (new Vector3 ((float)f1/2047*180, (float)f2/2047*180, (float)f3)/2047*180, Space.Self);

//		mCube.transform.Rotate(f1,f2,f3,Space.Self);
	}
}
