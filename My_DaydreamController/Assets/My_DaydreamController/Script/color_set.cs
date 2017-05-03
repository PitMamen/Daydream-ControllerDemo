using UnityEngine;
using System.Collections;
using System;

public class color_set : MonoBehaviour {

	void Start () {


	}

	// Update is called once per frame
	void Update () {
		OnGUI ();
	}

	void OnGUI()
	{
		//if (GUI.Button(new Rect(100, 100, 100, 100), "测试Mesh"))

			Mesh mesh = new Mesh();
			mesh = this.gameObject.GetComponent<MeshFilter>().mesh;
			int[] triangles;
			mesh.subMeshCount = 6;
			this.gameObject.GetComponent<MeshRenderer>().materials = new Material[6];
			triangles = mesh.triangles;
			mesh.SetTriangles(GetRangeArray(triangles, 0, 5), 0);
			mesh.SetTriangles(GetRangeArray(triangles, 6, 11), 1);
			mesh.SetTriangles(GetRangeArray(triangles, 12, 17), 2);
			mesh.SetTriangles(GetRangeArray(triangles, 18, 23), 3);
			mesh.SetTriangles(GetRangeArray(triangles, 24, 29), 4);
			mesh.SetTriangles(GetRangeArray(triangles, 30, 35), 5);
			this.gameObject.GetComponent<MeshRenderer>().materials[0].color = Color.red;
			this.gameObject.GetComponent<MeshRenderer>().materials[1].color = Color.green;
	    	this.gameObject.GetComponent<MeshRenderer>().materials [2].color = Color.gray;
			this.gameObject.GetComponent<MeshRenderer>().materials[3].color = Color.black;
			this.gameObject.GetComponent<MeshRenderer>().materials[4].color = Color.blue;
			this.gameObject.GetComponent<MeshRenderer>().materials[5].color = Color.yellow;
		  
		
	}
	public int[] GetRangeArray(int[] SourceArray, int StartIndex, int EndIndex)
	{
		try
		{
			int[] result = new int[EndIndex - StartIndex + 1];
			for (int i = 0; i <= EndIndex - StartIndex; i++) result[i] = SourceArray[i + StartIndex];
			return result;
		}
		catch (IndexOutOfRangeException ex)
		{
			throw new Exception(ex.Message);
		}
	}
}
