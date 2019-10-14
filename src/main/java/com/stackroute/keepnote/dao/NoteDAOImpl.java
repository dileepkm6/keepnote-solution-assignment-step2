package com.stackroute.keepnote.dao;

import com.stackroute.keepnote.model.Note;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/*
 * This class is implementing the NoteDAO interface. This class has to be annotated with @Repository
 * annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, thus 
 * 				 clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */
@Repository
@Transactional
public class NoteDAOImpl implements NoteDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.
	 */
	@Autowired
	SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public NoteDAOImpl(SessionFactory sessionFactory)
	{
		this.sessionFactory=sessionFactory;
	}

	/*
	 * Save the note in the database(note) table.
	 */

	public boolean saveNote(Note note) {
		Session session=sessionFactory.getCurrentSession();
		Note exitingNote=session.get(Note.class,note.getNoteId());
		if(exitingNote==null)
		{
			session.save(note);
			session.flush();
			return true;
		}
		return false;

	}

	/*
	 * Remove the note from the database(note) table.
	 */

	public boolean deleteNote(int noteId) {
		try
		{
			Session session=sessionFactory.getCurrentSession();
			Note note=session.get(Note.class,noteId);
			session.delete(note);
			session.flush();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/*
	 * retrieve all existing notes sorted by created Date in descending
	 * order(showing latest note first)
	 */
	public List<Note> getAllNotes() {
		Session session=sessionFactory.getCurrentSession();
		List<Note> noteList=session.createQuery("From Note").list();
		session.flush();
		return noteList;

	}

	/*
	 * retrieve specific note from the database(note) table
	 */
	public Note getNoteById(int noteId) {

		Session session=sessionFactory.getCurrentSession();
		Note note=session.get(Note.class,noteId);
		session.flush();
		return note;

	}

	/* Update existing note */

	public boolean UpdateNote(Note note) {
		Session session=sessionFactory.getCurrentSession();
		Note noteForUpdate=session.get(Note.class,note.getNoteId());
		if(noteForUpdate==null)
		{
			return false;
		}
		noteForUpdate.setNoteTitle(note.getNoteTitle());
		noteForUpdate.setNoteContent(note.getNoteContent());
		noteForUpdate.setNoteStatus(note.getNoteStatus());
		session.update(noteForUpdate);
		session.flush();
		return true;

	}

}
