package code;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "blocos")
public class BlockListWrapper {
	
	private List<Block> blocos;
	private List<SerialBlock> serialBlocos;
	
	@XmlElement(name = "bloco")
	public List<Block> getBlocks() {
		return blocos;
	}
	
	@XmlElement(name = "sbloco")
	public List<SerialBlock> getSerialBlocks() {
		return serialBlocos;
	}
		
	public void setBlocks(List<Block> blocos) {
		this.blocos = blocos;
	}
	
	public void setSerialBlocks(List<SerialBlock> blocos) {
		this.serialBlocos = blocos;
	}
}
