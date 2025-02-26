/**
 * #username - yairzaider
 * #id1      - 207030149
 * #name1    - Yair Zaider
 * #id2      - 207412347
 * #name2    - Afik Nitzan
 *
 */



public class BinomialHeap
{

	public int size;
	public HeapNode last;
	public HeapNode min;
	public int trees_number;


	//getters and setters to all fields

	public int get_trees_number()
	{
		return this.trees_number;
	}
	public void set_trees_number(int trees_number)
	{
		this.trees_number = trees_number;
	}

	public int get_size()
	{
		return this.size;
	}
	public void set_size(int size)
	{
		this.size=size;
	}
	public HeapNode get_last()
	{
		return this.last;
	}
	public void set_last(HeapNode last)
	{
		this.last=last;
	}

	public HeapNode get_min()
	{
		return this.min;
	}
	public void set_min(HeapNode min)
	{
		this.min=min;
	}
	// a basic constructor
	public BinomialHeap(int size, HeapNode last, HeapNode min,int trees_number)
	{
		this.size=size;
		this.last=last;
		this.min=min;
		this.trees_number= trees_number;

	}
	// an empty constructor
	public BinomialHeap()
	{
		this.size=0;
		this.last=null;
		this.min=null;
		this.trees_number=0;

	}

	/**
	 *
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 *  complexity:  O(log(n))
	 */
	public HeapItem insert(int key, String info)
	{
		HeapNode new_node=new HeapNode(null,null,null,null,0);
		HeapItem new_item= new HeapItem(new_node,key,info);
		new_node.set_item(new_item);
		new_node.set_next(new_node);
		new_item.set_node(new_node);
		BinomialHeap new_bin= new BinomialHeap(1, new_node,new_node,1);

		this.meld(new_bin);
		this.update_trees_num();
		return new_item;

	}
	/**
	 *
	 * pre: node!=null
	 * creating a new heap from node's children
	 *
	 *  complexity: O(log(n))
	 */

	public void update_trees_num()
	{
		HeapNode after_meld_last = this.get_last();
		int count=0;
		if(after_meld_last!=null)

		{
			count=1;
			HeapNode curr_node=after_meld_last.get_next();
			while(curr_node!=after_meld_last)
			{
				count++;
				curr_node=curr_node.get_next();
			}

		}
		this.set_trees_number(count);
	}



	public BinomialHeap build_children_heap(HeapNode node)
	{
		HeapNode children_heap_last = node.get_child();
		HeapNode children_heap_min = node.get_child();
		int counter =1;
		HeapNode trees_counter_curr=children_heap_last.get_next();
		while(trees_counter_curr!=children_heap_last)
		{
			counter++;
			trees_counter_curr=trees_counter_curr.get_next();
		}
		BinomialHeap children_heap = new BinomialHeap((int) (Math.pow( 2,node.get_rank())-1) , children_heap_last,children_heap_min,counter);
		children_heap.update_min();
		children_heap_last =children_heap.get_last();
		HeapNode children_heap_curr=children_heap.get_last().get_next();
		children_heap_last.set_parent(null);
		while(children_heap_curr!=children_heap_last)
		{
			children_heap_curr.set_parent(null);
			children_heap_curr=children_heap_curr.get_next();
		}
		return children_heap;
	}
	/**
	 * updating the field last after deletion of a node from heap
	 *
	 * complexity: O(log(n))
	 */
	public void update_last_after_deletion() {
		if (this.get_last() == null) { // No nodes in the heap
			return;
		}
		HeapNode new_last = this.get_last().get_next();
		HeapNode new_last_search_curr = new_last.get_next();
		while (new_last_search_curr != this.get_last().get_next()) {
			if (new_last_search_curr.get_rank() > new_last.get_rank()) {
				new_last = new_last_search_curr;
			}
			new_last_search_curr = new_last_search_curr.get_next();
		}
		this.set_last(new_last);
	}
	/**
	 *
	 * Deletes the minimal item
	 * complexity: O(log(n))
	 */
	public void deleteMin()
	{
		HeapNode min_node = this.get_min();

		if (min_node == null)
		{
			return; // No nodes in the heap
		}
		int deleted_node_rank = min_node.get_rank();

		if (this.get_size() == 1)
		{ // The case in which there is only one node in the heap
			this.set_last(null);
			this.set_size(0);
			this.set_trees_number(0);
			this.set_min(null);

			return;
		}

		if (min_node.get_child() == null)
		{ // Min node has no children
			HeapNode temp = min_node.get_next();
			HeapNode deletion_search_curr = this.get_last();
			while (deletion_search_curr.get_next() != min_node) {
				deletion_search_curr = deletion_search_curr.get_next();
			}
			deletion_search_curr.set_next(temp);
			this.set_size(this.get_size() - 1);
			if (min_node == this.get_last()) {
				this.update_last_after_deletion();
			}

			this.update_min();
			return;
		}
		else
		{ // Min node has children
			BinomialHeap min_node_children_heap = build_children_heap(min_node);
			if (this.get_trees_number() == 1) {
				this.set_last(null);
				this.set_size(0);
				this.set_trees_number(0);
				this.set_min(null);
			} else {
				HeapNode temp = min_node.get_next();
				HeapNode deletion_search_curr = this.get_last();
				while (deletion_search_curr.get_next() != min_node) {
					deletion_search_curr = deletion_search_curr.get_next();
				}
				deletion_search_curr.set_next(temp);
				this.set_size(this.get_size() - ((int) Math.pow(2, min_node.get_rank())));
				if (min_node == this.get_last()) {
					this.update_last_after_deletion();
				}
			}

			this.update_trees_num();
			this.meld(min_node_children_heap);

			this.update_min();
			this.update_trees_num();

		}
	}


	/**
	 *
	 * Return the minimal HeapItem, null if empty.
	 * complexity: O(log(n))
	 */
	public HeapItem findMin()
	{
		HeapNode heap_node_min = this.min;
		if(heap_node_min!=null)
		{
			return heap_node_min.get_item();
		}
		return null;
	}

	/**
	 *
	 * updates min field of heap
	 *
	 *
	 * complexity: O(log(n))
	 *
	 */
	public void update_min() {
		if (this.get_last() == null) {
			this.set_min(null); // No nodes in the heap
			return;
		}
		HeapNode min_node = this.get_last();
		HeapNode new_min_search_start = this.get_last();
		HeapNode new_min_search_curr = this.get_last().get_next();
		while (new_min_search_curr != new_min_search_start) {
			if (new_min_search_curr.get_item().get_key() < min_node.get_item().get_key()) {
				min_node = new_min_search_curr;
			}
			new_min_search_curr = new_min_search_curr.get_next();
		}
		this.update_trees_num();
		this.set_min(min_node);
	}

	/**
	 *
	 * pre: 0<diff<item.key
	 *
	 * Decrease the key of item by diff and fixes the heap.
	 * complexity:  O(log(n))
	 */

	public void decreaseKey(HeapItem item, int diff)
	{
		item.set_key(item.get_key()-diff);
		HeapNode curr= item.get_node();
		while( curr.get_parent()!= null )
		{
			if (curr.get_item().get_key() < curr.get_parent().get_item().get_key())
			{
				int tmp_key = curr.get_item().get_key();
				String tmp_info = curr.get_item().get_info();

				curr.get_item().set_key(curr.get_parent().get_item().get_key());
				curr.get_item().set_info(curr.get_parent().get_item().get_info());
				curr.get_parent().get_item().set_key(tmp_key);
				curr.get_parent().get_item().set_info(tmp_info);
				curr=curr.get_parent();
			}
			else
			{
				break;
			}


		}
		this.update_trees_num();
		this.update_min();
	}

	/**
	 *
	 * Delete the item from the heap.
	 * complexity:  O(log(n))
	 */
	public void delete(HeapItem item)
	{
		this.decreaseKey(item, item.get_key());
		this.deleteMin();

		return;
	}

	/**
	 *
	 * Meld the heap with heap2
	 * //complexity: O(log(n)) when n is the size of the larger heap
	 */

	public void meld(BinomialHeap heap2) {
		if (heap2.get_size() == 0)
		{
			return;
		}
		if (this.get_size() == 0)
		{
			this.set_last(heap2.get_last());
			this.set_min(heap2.get_min());
			this.set_size(heap2.get_size());
			this.set_trees_number(heap2.get_trees_number());
			return;
		}

		if(this.get_size()%2==0 && heap2.get_size()==1)
		{
			heap2.get_last().set_next(this.get_last().get_next());
			this.set_size(this.get_size()+1);
			this.set_trees_number(this.get_trees_number()+1);
			this.get_last().set_next(heap2.get_last());
			if(heap2.get_min().get_item().get_key()<this.get_min().get_item().get_key())
			{
				this.set_min(heap2.get_min());
			}
			return;
		}


		int heap2_size = heap2.get_size();
		int arrays_size = Math.max(this.get_last().get_rank() ,heap2.get_last().get_rank()) +4;
		HeapNode[] trees_array_heap1 = new HeapNode[arrays_size];
		HeapNode[] trees_array_heap2 = new HeapNode[arrays_size];
		HeapNode[] combined_heaps_tree_array = new HeapNode[arrays_size];

		HeapNode curr_search_trees_heap1 = this.get_last().get_next();
		trees_array_heap1[this.get_last().get_rank()] = this.get_last();
		while (curr_search_trees_heap1 != this.get_last())      /// building the array for heap1 . in trees_array_heap1[i] will be the root node of the tree with rank i
		{
			trees_array_heap1[curr_search_trees_heap1.get_rank()] = curr_search_trees_heap1;
			curr_search_trees_heap1 = curr_search_trees_heap1.get_next();
		}

		HeapNode curr_search_trees_heap2 = heap2.get_last().get_next();
		trees_array_heap2[heap2.get_last().get_rank()] = heap2.get_last();
		while (curr_search_trees_heap2 != heap2.get_last())    /// building the array for heap2 . in trees_array_heap2[i] will be the root node of the tree with rank i
		{
			trees_array_heap2[curr_search_trees_heap2.get_rank()] = curr_search_trees_heap2;
			curr_search_trees_heap2 = curr_search_trees_heap2.get_next();
		}

		HeapNode carry = null;
		for (int i = 0; i < combined_heaps_tree_array.length; i++) // performing binary adding of heap's trees
		{
			if (trees_array_heap1[i] != null && trees_array_heap2[i] == null && carry == null)
			{
				combined_heaps_tree_array[i] = trees_array_heap1[i];
				carry = null;
			} else if (trees_array_heap1[i] == null && trees_array_heap2[i] != null && carry == null) {
				combined_heaps_tree_array[i] = trees_array_heap2[i];
				carry = null;
			} else if (trees_array_heap1[i] != null && trees_array_heap2[i] != null && carry == null)
			{
				carry = HeapNode.link_trees(trees_array_heap1[i], trees_array_heap2[i]);
			} else if (trees_array_heap1[i] == null && trees_array_heap2[i] != null && carry != null) {
				carry = HeapNode.link_trees(trees_array_heap2[i], carry);
			} else if (trees_array_heap1[i] != null && trees_array_heap2[i] == null && carry != null) {
				carry = HeapNode.link_trees(trees_array_heap1[i], carry);
			} else if (trees_array_heap1[i] == null && trees_array_heap2[i] == null && carry != null) {
				combined_heaps_tree_array[i] = carry;
				carry = null;
			} else if (trees_array_heap1[i] != null && trees_array_heap2[i] != null && carry != null)
			{
				int[] three_tree_key_array = {trees_array_heap1[i].get_item().get_key(), trees_array_heap2[i].get_item().get_key(), carry.get_item().get_key()};
				int minKey = arr_min_val(three_tree_key_array);
				HeapNode minNode, firstLink, secondLink;

				if (minKey == trees_array_heap1[i].get_item().get_key())
				{
					minNode = trees_array_heap1[i];
					firstLink = trees_array_heap2[i];
					secondLink = carry;
				} else if (minKey == trees_array_heap2[i].get_item().get_key()) {
					minNode = trees_array_heap2[i];
					firstLink = trees_array_heap1[i];
					secondLink = carry;
				} else {
					minNode = carry;
					firstLink = trees_array_heap1[i];
					secondLink = trees_array_heap2[i];
				}

				combined_heaps_tree_array[i] = minNode;
				carry = HeapNode.link_trees(firstLink, secondLink);

			}
		}

		int trees_amount_counter = 0;
		for (int i = 0; i < combined_heaps_tree_array.length; i++) { // counts new num of trees after melding
			if (combined_heaps_tree_array[i] != null) {
				trees_amount_counter++;
			}
		}

		int j = 0;
		HeapNode[] construct_heap_array = new HeapNode[trees_amount_counter];
		for (int i = 0; i < combined_heaps_tree_array.length; i++)     // creating an array consisting only with non-null root trees
		{
			if (combined_heaps_tree_array[i] != null) {
				construct_heap_array[j] = combined_heaps_tree_array[i];
				j++;
			}
		}

		for (int i = 0; i < construct_heap_array.length - 1; i++)   // creating the melded binomial heap from the array of trees

		{
			construct_heap_array[i].set_next(construct_heap_array[i + 1]);
		}
		construct_heap_array[construct_heap_array.length - 1].set_next(construct_heap_array[0]);

		// updating fields of melded heap
		this.set_last(construct_heap_array[construct_heap_array.length - 1]);
		this.set_size(this.get_size() + heap2_size);
		this.set_trees_number(construct_heap_array.length);
		this.update_trees_num();
		this.update_min();
	}


	/**
	 * pre : arr[]!=null
	 * returns the min value of array
	 * complexity: O(n)
	 */
	public static int arr_min_val(int[] arr)
	{
		int min = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] < min) {
				min = arr[i];
			}
		}
		return min;
	}
	/**
	 *
	 * Return the number of elements in the heap
	 * complexity: O(1)
	 */
	public int size()
	{
		return this.get_size();
	}

	/**
	 *
	 * The method returns true if and only if the heap
	 * is empty.
	 *   //complexity: O(1)
	 */
	public boolean empty()
	{

		return this.get_size()==0;
	}

	/**
	 *
	 * Return the number of trees in the heap.
	 * //complexity: O(1)
	 */
	public int numTrees()
	{
		return this.get_trees_number();
	}

	/**
	 * Class implementing a node in a Binomial Heap.
	 *
	 */
	public static class HeapNode {
		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;

		public HeapNode(HeapItem item, HeapNode child, HeapNode next, HeapNode parent, int rank) {
			this.item = item;
			this.child = child;
			this.next = next;
			this.parent = parent;
			this.rank = rank;
		}

		// getters and setters to all fields
		public HeapItem get_item() {
			return this.item;
		}

		public void set_item(HeapItem item) {
			this.item = item;
		}

		public HeapNode get_child() {
			return this.child;
		}

		public void set_child(HeapNode child) {
			this.child = child;
		}

		public HeapNode get_next() {
			return this.next;
		}

		public void set_next(HeapNode next) {
			this.next = next;
		}

		public HeapNode get_parent() {
			return this.parent;
		}

		public void set_parent(HeapNode parent) {
			this.parent = parent;
		}

		public int get_rank() {
			return this.rank;
		}

		public void set_rank(int rank) {
			this.rank = rank;
		}

		/**
		 * pre: root_tree1.get_rank()==root_tree2.get_rank()
		 * linking 2 trees of rank i and returns root of new tree of rank i+1
		 * complexity: O(1)
		 */
		public static HeapNode link_trees(HeapNode root_tree1, HeapNode root_tree2) {
			HeapNode smaller;
			HeapNode larger;

			// Determine which root has the smaller key
			if (root_tree1.get_item().get_key() < root_tree2.get_item().get_key()) {
				smaller = root_tree1;
				larger = root_tree2;
			} else {
				smaller = root_tree2;
				larger = root_tree1;
			}

			// Link the larger tree as a child of the smaller tree
			larger.set_parent(smaller);
			if (smaller.get_child() == null)
			{
				// If the smaller tree has no children, set the larger tree as its only child
				smaller.set_child(larger);
				larger.set_next(larger);
			}
			else
			{
				// Otherwise, insert the larger tree into the circular list of children
				HeapNode firstChild = smaller.get_child();
				HeapNode lastChild = firstChild.get_next();

				firstChild.set_next(larger);
				larger.set_next(lastChild);
				smaller.set_child(larger);
			}

			// Increase the rank of the smaller tree
			smaller.set_rank(smaller.get_rank() + 1);

			return smaller;
		}
	}
	/**
	 * Class implementing an item in a Binomial Heap.
	 *
	 */
	public static class HeapItem
	{
		public HeapNode node;
		public int key;
		public String info;

		public HeapItem(HeapNode node, int key, String info)
		{
			this.node=node;
			this.key=key;
			this.info=info;
		}
		// getters and setters to all fields
		public HeapNode get_node()
		{
			return this.node;
		}
		public void set_node(HeapNode node)
		{
			this.node = node;
		}
		public int get_key()
		{
			return this.key;
		}
		public void set_key(int key)
		{
			this.key = key;
		}
		public String get_info()
		{
			return this.info;
		}
		public void set_info(String info)
		{
			this.info = info;
		}
	}



}