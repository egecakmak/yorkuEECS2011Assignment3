package A3Q1;

/**
 * Extends the TreeMap class to allow convenient access to entries within a
 * specified range of key values (findAllInRange).
 * 
 * @author jameselder
 */
public class BSTRange<K, V> extends TreeMap<K, V> {

	/*
	 * Returns the lowest (deepest) position in the subtree rooted at pos that is a
	 * common ancestor to positions with keys k1 and k2, or to the positions they
	 * would occupy were they present.
	 */
	protected Position<Entry<K, V>> findLowestCommonAncestor(K k1, K k2, Position<Entry<K, V>> pos) {
		if (!isInternal(pos)) //Checks if at a node that is not empty.
			return null;
		//System.out.println("flcavisit " + pos.getElement().getKey());
		K posKey = pos.getElement().getKey();
		int k1CompareValue = compare(k1, posKey);
		int k2CompareValue = compare(k2, posKey);
		if (k1CompareValue == 1 && k2CompareValue == 1) { // Go right if both nodes are in that side.
			return findLowestCommonAncestor(k1, k2, right(pos));
		} else if (k1CompareValue == -1 && k2CompareValue == -1) { // Go left if both nodes are in that side.
			return findLowestCommonAncestor(k1, k2, left(pos));
		} else { //If both nodes are at the same position or in different sides than return the position.
			return pos;
		}
	}
	/*
	 * Finds all entries in the subtree rooted at pos with keys of k or greater and
	 * copies them to L, in non-decreasing order.
	 */
	protected void findAllAbove(K k, Position<Entry<K, V>> pos, PositionalList<Entry<K, V>> L) {
		if(isInternal(pos)) { //Checks if at a node that is not empty.
			K posKey = pos.getElement().getKey();
			//System.out.println("Visiting " + pos.getElement().getKey());
			/*if(compare(k,posKey) <= -1)*/ findAllAbove(k,left(pos),L); // Checks left side of the given node.
			if(compare(posKey, k) <= 0) L.addLast(pos.getElement()); // Adds the node if it meets the requirement.
			if(compare(k,posKey) >= 1) findAllAbove(k,right(pos),L); // Checks right side of the given node if it should.
		}
	}

	/*
	 * Finds all entries in the subtree rooted at pos with keys of k or less and
	 * copies them to L, in non-decreasing order.
	 */
	protected void findAllBelow(K k, Position<Entry<K, V>> pos, PositionalList<Entry<K, V>> L) {
		if(isInternal(pos)) { //Checks if at a node that is not empty.
			//System.out.println("Visiting " + pos.getElement().getKey());
			K posKey = pos.getElement().getKey();
			if(compare(k,posKey) <= -1) findAllBelow(k,left(pos),L); // Checks left side of the given node if it should.
			if(compare(posKey, k) >= 0) L.addLast(pos.getElement()); // Adds the node if it meets the requirement.
			/*if(compare(k,posKey) >= 1)*/ findAllBelow(k,right(pos),L); // Checks right side of the given node.
		}
	}
	
	/*
	 * Returns all entries with keys no less than k1 and no greater than k2, in
	 * non-decreasing order.
	 */
	public PositionalList<Entry<K, V>> findAllInRange(K k1, K k2) {
		PositionalList<Entry<K, V>> listToReturn = new LinkedPositionalList<>();
		if (k1 == null || k2 == null || tree.size() == 1 || compare(k1, k2) > 0) { // Check for exception cases and return an empty list if any of them are true.
			return listToReturn;
		} else {
			Position<Entry<K, V>> lowestCommonAncestor = findLowestCommonAncestor(k1, k2, root());
			if (lowestCommonAncestor == null) { // If there is no lowest common ancestor return an empty list.
				return listToReturn;
			} else {
				//System.out.println("LCA :" + lowestCommonAncestor.getElement().getKey());
				findAllBelow(k1, left(lowestCommonAncestor), listToReturn); // Process all elements less than the common ancestor.

				//System.out.println("Visiting " + lowestCommonAncestor.getElement().getKey());
				listToReturn.addLast(lowestCommonAncestor.getElement()); // Add the common ancestor inorderly.
				
				findAllAbove(k2, right(lowestCommonAncestor), listToReturn); // Process all elements greater than the common ancestor.
				return listToReturn;
			}
		}
	}
}