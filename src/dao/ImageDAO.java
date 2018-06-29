package dao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Format;
import model.Image;

@Stateful
public class ImageDAO {
	
	@PersistenceContext(name="ImageConvertPU")
	EntityManager em;
	
	public String save(byte[] data, String meta) {
		Image img = new Image();
		img.setData(data);
		String name = System.currentTimeMillis()+"-"+UUID.randomUUID();
		img.setName(name);
		img.setMeta(meta);
		em.persist(img);
		
		@SuppressWarnings("unchecked")
		List<Format> formats = em.createQuery("from Format f").getResultList();
		for(Format f : formats){
			Image imgOutput;
			try {
				imgOutput = convert(img, f);
				em.persist(imgOutput);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return name;
	}
	
	private Image convert(Image img, Format f) throws InterruptedException {
		try {
			File fin = File.createTempFile(img.getName()+"In", img.getMeta());
			FileOutputStream fout0 = new FileOutputStream(fin);
			fout0.write(img.getData());
			fout0.close();
			
			File fout = File.createTempFile(img.getName()+"Out", f.getExtension());
			
			StringBuilder builder = new StringBuilder();
			for(String key : f.getProperties().keySet()) {
				String value = f.getProperties().get(key);
				builder.append(" -");
				builder.append(key);
				builder.append(" ");
				builder.append(value);
				builder.append(" ");
			}
			
			String cmd = String.format("%s %s %s %s",
											"convert",
											fin.getAbsolutePath(),
											builder.toString(),
											fout.getAbsolutePath());
			System.out.println(cmd);
			
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			
			FileInputStream fins = new FileInputStream(fout);
			System.out.println(fout.getAbsolutePath());
			
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] tab = new byte[1024];
			int length = fins.read(tab);
			while(length != -1) {
				bout.write(tab, 0, length);
				bout.flush();
				length = fins.read(tab);
			}
			fins.close();
			bout.close();
			
			Image result = new Image();
			result.setData(bout.toByteArray());
			result.setFormat(f);
			result.setMeta(f.getExtension());
			result.setName(img.getName());
			return result;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Image findByName(String name, String format) throws InterruptedException {
		String query = "";
		if(format==null) {
			query = String.format("from Image i where i.name='%s' and i.format is null", name);
		} else {
			query = String.format("from Image i where i.name='%s' and i.format.name='%s'", name, format);
		}
		
		@SuppressWarnings("unchecked")
		List<Image> images = em.createQuery(query).getResultList();
		if(images.size()==0) {
			query = String.format("from Image i where i.name='%s' and i.format is null", name);
			Image img = (Image)em.createQuery(query).getSingleResult();
			Format f = em.find(Format.class, format);
			Image transf = convert(img, f);
			em.persist(transf);
			return transf;
		} else {
			return images.get(0);
		}
		
	}
	
	public Image update(Image i) {
		Image entity = em.merge(i);
		return entity;
	}
	
	public Image remove(Image i) {
		Image toRemove = em.find(Image.class, i.getName());
		em.remove(toRemove);
		return toRemove;
	}
}
