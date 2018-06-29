package dao;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Format;

@Stateful
public class FormatDAO {

		@PersistenceContext(name="ImageConvertPU")
		EntityManager em;
		
		public Format save(Format f) {
			em.persist(f);
			return f;
		}
		
		public Format find(String nom) {
			Format f = em.find(Format.class, nom);
			return f;
		}
		
		public Format update(Format f) {
			Format entity = em.merge(f);
			return entity;
		}
		
		public Format remove(Format f) {
			Format toRemove = em.find(Format.class, f.getName());
			em.remove(toRemove);
			return toRemove;
		}
}
