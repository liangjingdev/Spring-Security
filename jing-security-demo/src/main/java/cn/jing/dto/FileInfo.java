/**
 * 
 */
package cn.jing.dto;

/**
 * function:文件实体类
 * 
 * @author liangjing
 *
 */
public class FileInfo {

	public FileInfo(String path) {
		this.path = path;
	}

	public String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
