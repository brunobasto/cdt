/*******************************************************************************
 * Copyright (c) 2006 Wind River Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Markus Schorn - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.cdt.core.index;

import java.util.regex.Pattern;

import org.eclipse.cdt.core.dom.IName;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Interface for accessing the index for one or more projects.
 * 
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 * <p>
 * <strong>EXPERIMENTAL</strong>. This class or interface has been added as
 * part of a work in progress. There is no guarantee that this API will work or
 * that it will remain the same. Please do not use this API without consulting
 * with the CDT team.
 * </p>
 * 
 * @since 4.0
 */
public interface IIndex {
	/**
	 * Constant to specify infinite depth. 
	 * @see #findIncludedBy(IIndexFile, int) 
	 * @see #findIncludes(IIndexFile, int)
	 */
	final int DEPTH_INFINITE = -1;

	/**
	 * Constant to find direct includes, only. 
	 * @see #findIncludedBy(IIndexFile, int) 
	 * @see #findIncludes(IIndexFile, int)
	 */
	final int DEPTH_ZERO = 0;

	/** 
	 * Constant to search for declarations. This does not include definitions.
	 */
	final int FIND_DECLARATIONS = 0x1;
	/** 
	 * Constant to search for definitions. This does not include declarations.
	 */
	final int FIND_DEFINITIONS  = 0x2;
	/** 
	 * Constant to search for all declarations including definitons.
	 */
	final int FIND_REFERENCES   = 0x4;
	/** 
	 * Constant to search for references. This does not include declarations or definitions.
	 */
	final int FIND_DECLARATIONS_DEFINITIONS = FIND_DECLARATIONS | FIND_DEFINITIONS;
	/** 
	 * Constant to search for all occurrences of a binding. This includes declarations, definitons and references.
	 */
	final int FIND_ALL_OCCURENCES = FIND_DECLARATIONS | FIND_DEFINITIONS | FIND_REFERENCES;

	/**
	 * Before making calls to an index you have to obtain a lock. The objects 
	 * returned by an index become invalid as soon as the indexer writes to the
	 * index. You may obtain nested read locks. Make sure you release the lock.
	 * @see #getLastWriteAccess()
	 * <pre>
	 * index.acquireReadLock();
	 * try {
	 *    ....
	 * }
	 * finally {
	 *    index.releaseReadLock();
	 * }
	 * </pre>
	 */
	public void acquireReadLock() throws InterruptedException;
	
	/**
	 * Any lock obtained by {@link #acquireReadLock()} must be released.
	 */
	public void releaseReadLock();
	
	/**
	 * Returns a timestamp of when the index was last written to. This can
	 * be used to figure out whether information read from the index is 
	 * still reliable or not.
	 *
	 * <pre>
	 * long timestamp;
	 * IBinding binding= null;
	 * index.acquireReadLock();
	 * try {
	 *    timestamp= index.getLastWriteAccess();
	 *    binding= index.findBinding(...);
	 * }
	 * finally {
	 *    index.releaseReadLock();
	 * }
	 * ...
	 * index.acqureReadLock();
	 * try {
	 *    if (index.getLastWriteAccess() != timestamp) {
	 *       // don't use binding, it's not valid anymore
	 *       binding= index.findBinding(...);
	 *    }
	 *    String name= binding.getName();
	 *    ...
	 * }
	 * finally {
	 *    index.releaseReadLock();
	 * }
	 */
	public long getLastWriteAccess();
	
	/**
	 * Looks for a file with the given location. May return <code>null</code>.
	 * @param location absolute path of the file location
	 * @return the file in the index or <code>null</code>
	 * @throws CoreException
	 */
	public IIndexFile getFile(IPath location) throws CoreException;

	/**
	 * Looks for include relations originated by the given file.
	 * This is the same as <pre> findIncludes(file, DEPTH_ZERO); </pre> 
	 * @param file the file containing the include directives
	 * @return an array of include relations
	 * @throws CoreException
	 */
	public IIndexInclude[] findIncludes(IIndexFile file) throws CoreException;

	/**
	 * Looks for include relations pointing to the given file. 
	 * This is the same as <pre> findIncludedBy(file, DEPTH_ZERO); </pre> 
	 * @param file the file included by the directives to be found
	 * @return an array of include relations
	 * @throws CoreException
	 */
	public IIndexInclude[] findIncludedBy(IIndexFile file) throws CoreException;

	/**
	 * Looks recursively for include relations originated by the given file.
	 * @param file the file containing the include directives
	 * @param depth depth to which includes are followed, should be one of 
	 * {@link #DEPTH_ZERO} or {@link #DEPTH_INFINITE}
	 * @return an array of include relations
	 * @throws CoreException
	 */
	public IIndexInclude[] findIncludes(IIndexFile file, int depth) throws CoreException;

	/**
	 * Looks recursively for include relations pointing to the given file.
	 * @param file the file the include directives point to
	 * @param depth depth to which includes are followed, should be one of 
	 * {@link #DEPTH_ZERO} or {@link #DEPTH_INFINITE}
	 * @return an array of include relations
	 * @throws CoreException
	 */
	public IIndexInclude[] findIncludedBy(IIndexFile file, int depth) throws CoreException;

	/**
	 * Resolves the file that is included by the given include directive. May return <code>null</code>
	 * in case the file cannot be found. This is usually more efficient than using:
	 * <pre>
	 * getFile(include.getIncludesLocation())
	 * </pre>
	 * @param include
	 * @return the file included or <code>null</code>.
	 * @throws CoreException
	 * @since 4.0
	 */
	public IIndexFile resolveInclude(IIndexInclude include) throws CoreException;
	
	/**
	 * Searches for the binding of a name. The name may be originated by
	 * an AST or by a search in an index. May return <code>null</code>.
	 * @param name a name to find the binding for
	 * @return the binding or <code>null</code>
	 * @throws CoreException
	 */
	public IIndexBinding findBinding(IName name) throws CoreException;
	
	/**
	 * Searches for all bindings with simple names that match the given pattern. In case a binding exists 
	 * in multiple projects, no duplicate bindings are returned.
	 * This is fully equivalent to 
	 * <pre>
	 * findBindings(new Pattern[]{pattern}, isFullyQualified, filter, monitor);
	 * </pre> 
	 * @param pattern the pattern the name of the binding has to match.
	 * @param isFullyQualified if <code>true</code>, binding must be in global scope
	 * @param filter a filter that allows for skipping parts of the index 
	 * @param monitor a monitor to report progress
	 * @return an array of bindings matching the pattern
	 * @throws CoreException
	 */
	public IIndexBinding[] findBindings(Pattern pattern, boolean isFullyQualified, IndexFilter filter, IProgressMonitor monitor) throws CoreException;

	/**
	 * Searches for all bindings with qualified names that seen as an array of simple names match the given array 
	 * of patterns. In case a binding exists in multiple projects, no duplicate bindings are returned.
	 * You can search with an array of patterns that specifies a partial qualification only. 
	 * @param patterns an array of patterns the names of the qualified name of the bindings have to match.
	 * @param isFullyQualified if <code>true</code>, the array of pattern specifies the fully qualified name
	 * @param filter a filter that allows for skipping parts of the index 
	 * @param monitor a monitor to report progress
	 * @return an array of bindings matching the pattern
	 * @throws CoreException
	 */
	public IIndexBinding[] findBindings(Pattern[] patterns, boolean isFullyQualified, IndexFilter filter, IProgressMonitor monitor) throws CoreException;
	
	/**
	 * Searches for all names that resolve to the given binding. You can limit the result to references, declarations
	 * or definitions, or a combination of those.
	 * @param binding a binding for which names are searched for
	 * @param flags a combination of {@link #FIND_DECLARATIONS}, {@link #FIND_DEFINITIONS} and {@link #FIND_REFERENCES}
	 * @return an array of names
	 * @throws CoreException
	 */
	public IIndexName[] findNames(IBinding binding, int flags) throws CoreException;

	/**
	 * Searches for all references that resolve to the given binding. 
	 * This is fully equivalent to 
	 * <pre>
	 * findNames(binding, IIndex.FIND_REFERENCES);
	 * </pre> 
	 * @param binding a binding for which references are searched for
	 * @return an array of names
	 * @throws CoreException
	 */
	public IIndexName[] findReferences(IBinding binding) throws CoreException;

	/**
	 * Searches for all declarations and definitions that resolve to the given binding. 
	 * This is fully equivalent to 
	 * <pre>
	 * findNames(binding, IIndex.FIND_DECLARATIONS_DEFINITIONS);
	 * </pre> 
	 * @param binding a binding for which declarations are searched for
	 * @return an array of names
	 * @throws CoreException
	 */
	public IIndexName[] findDeclarations(IBinding binding) throws CoreException;

	/**
	 * Searches for all definitions that resolve to the given binding. 
	 * This is fully equivalent to 
	 * <pre>
	 * findNames(binding, IIndex.FIND_DEFINITIONS);
	 * </pre> 
	 * @param binding a binding for which declarations are searched for
	 * @return an array of names
	 * @throws CoreException
	 */
	public IIndexName[] findDefinitions(IBinding binding) throws CoreException;
}
