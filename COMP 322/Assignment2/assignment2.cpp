/* Philippe Bergeron */

#include <iostream>
#include <cstdlib>

using namespace std;

class Node {
    public:
        int data;
        Node* next;
        Node* previous;

        // Constructors
        Node();
        Node(int data, Node* next, Node* previous);

        // Destructor
        ~Node();
};

Node::Node(){
    this->data = 0;
    this->next = nullptr;
    this->previous = nullptr;
}


Node::Node(int data, Node* next, Node* previous){
    this->data = data;
    this->next = next;
    this->previous = previous;
}
Node::~Node(){
    
}

class DLLStructure {
    public:
        DLLStructure();
        DLLStructure(int array[], int size);
        ~DLLStructure();
        DLLStructure(DLLStructure& dlls);

        void PrintDLL();
        void InsertAfter(int valueToInsertAfter, int valueToBeInserted);
        void InsertBefore(int valueToInsertBefore, int valueToBeInserted);
        void Delete(int value);
        void Swap(Node* a, Node* b);
        void Sort();
        bool IsEmpty();
        int GetHead();
        int GetTail();
        int GetSize();
        int GetMax();
        int GetMin();
    
    private:
        Node* first;
        Node* last;
        bool empty;
        int size;
        int max;
        int min;
};

DLLStructure::DLLStructure(){
    this->first = new Node();
    this->last = new Node();
}

DLLStructure::DLLStructure(int array[], int size){
    if (size>0){
        this->size = size;
        this->empty = false;
        this->max = array[0];
        this->min = array[0];
        
        Node* curr;
        curr = new Node();
        Node* prev = nullptr;
        for (int i=0;i<size;i++){
            if(this->max<array[i]){
                this->max = array[i];
            }
            if(this->min>array[i]){
                this->min = array[i];
            }
            if (i==0){
                curr->data = array[i];
                
            /* if array has  one element then
            this element is first and last */
                if (size < 2){
                    curr->previous = nullptr;
                    curr->next = nullptr;
                    this->first = curr;
                    this->last = curr;
                } else { 
                    curr->previous = nullptr;
                    curr->next = new Node();
                    this->first = curr;
                    prev = curr;
                    curr = curr->next;
             }
            

            } else if (i==size-1){
                curr->data = array[i];
                curr->next = nullptr;
                curr->previous = prev;
                this->last = curr;
            } else {
                curr->data = array[i];
                curr->next = new Node();
                curr->previous = prev;
                prev = curr;
                curr = curr->next;
            }
        
        }

    }
    
}

DLLStructure::~DLLStructure(){
    Node* curr = this->first;
    Node* next = curr->next;
    while (curr != this->last){
        delete curr;
        curr = next;
        next = curr->next;

    }
    delete curr;
    delete next;
}

void DLLStructure::PrintDLL(){
    if (!IsEmpty()){
        Node* curr;
        curr = this->first;
        while (curr != this->last){
            int element = curr->data;
            cout << element;
            cout << ", ";
            curr = curr->next;
        }
        cout << curr->data << endl;
    } else {
        cout <<  endl;
    }
    
}

DLLStructure::DLLStructure(DLLStructure& dlls){
    DLLStructure* DLLcopy = new DLLStructure();
    Node* curr = (&dlls)->first;
    Node* toput = new Node(curr->data,curr->previous,curr->previous);
    this->first = toput;
    Node* pointer =  this->first;
    curr = curr->next;
    while (curr != (&dlls)->last){
        Node* toput = new Node(curr->data,curr->previous,curr->previous);
        pointer->next = toput;
        pointer = pointer->next;
        curr = curr->next;
    }

        toput = new Node(curr->data,curr->previous,curr->previous);
        pointer->next = toput;
        this->last = toput;




    


}

void DLLStructure::InsertAfter(int valueToInsertAfter, int valueToBeInserted){
    if (IsEmpty()){
        this->empty = false;
    }
    if(this->max<valueToBeInserted){
        this->max = valueToBeInserted;
        }
    if(this->min>valueToBeInserted){
        this->min = valueToBeInserted;
        }
    Node* curr = this->first;
    while (curr->data != valueToInsertAfter && curr != this->last){
        curr = curr->next;
    }
    Node* old = curr->next;
    Node* newNode = new Node(valueToBeInserted, old, curr);
    curr->next = newNode;
    if (curr == this->last){
        this->last = newNode;
    } else {
        old->previous = newNode;  
    }
    
    this->size += 1;
    
    //old->previous = newNode;
}

void DLLStructure::InsertBefore(int valueToInsertBefore, int valueToBeInserted){
    if (IsEmpty()){
        this->empty = false;
    }
    Node* curr = this->first;
    while (curr->data != valueToInsertBefore && curr != this->last){
        curr = curr->next;
    }
    if (curr->previous == nullptr){
        if(this->max<valueToBeInserted){
            this->max = valueToBeInserted;
        }
        if(this->min>valueToBeInserted){
            this->min = valueToBeInserted;
        }
        Node* oldNode = this->first;
        Node* newNode = new Node(valueToBeInserted,oldNode,nullptr);
        this->first = newNode;
        oldNode->previous = newNode;
        this->size += 1;
    } else if (curr->data == valueToInsertBefore){
        curr = curr->previous;
        InsertAfter(curr->data,valueToBeInserted);
    } else{
        if(this->max<valueToBeInserted){
            this->max = valueToBeInserted;
        }
        if(this->min>valueToBeInserted){
            this->min = valueToBeInserted;
        }
        Node* oldNode = this->first;
        Node* newNode = new Node(valueToBeInserted,oldNode,curr);
        oldNode->previous = newNode;
        this->first = newNode;
        this->size += 1;
    }
    
}

void DLLStructure::Delete(int value){
    Node* curr = this->first;
    while (curr->data != value && curr != this->last){
        curr = curr->next;
    }

    if (curr->data == value){
        
        this->size -= 1;
        if (curr==this->first && curr==this->last){
            delete curr;
            //this->first = nullptr;
            //this->last = nullptr;
            this->empty = true;
        }
        
        else if (curr == this->first){
            
            this->first = curr->next;
            delete curr;
        } else if (curr == this->last){
            
            this->last = curr->previous;
            delete curr;
        } else {
            (curr->previous)->next = curr->next;
            (curr->next)->previous = curr->previous;
            delete curr;
        }
    }
    curr = this->first;
    this->max = curr->data;
    this->min = curr->data;
    while (curr!= this->last){
       if (curr->data > this->max) {
           this->max = curr->data;
       }
       if (curr->data < this->min) {
           this->min = curr->data;
       }
       curr = curr->next;
    }

}
void DLLStructure::Swap(Node* a, Node* b){
    
    int adata = a->data;
    int bdata = b->data;
    a->data = bdata;
    b->data = adata;
    
     

}
//Bubble sort
void DLLStructure::Sort(){
    Node* curr1 = this->first;
    Node* upTo = this->last;
    while (curr1 != this->last){
        Node* curr2 = this->first;
        while (curr2 != upTo){
            if (curr2->data > (curr2->next)->data){

                Swap(curr2,curr2->next);
            }
            curr2 = curr2->next;
        }
        curr1 = curr1->next;
        upTo = upTo->previous;
    }
    
}

bool DLLStructure::IsEmpty(){
    return this->empty;
}

int DLLStructure::GetHead(){
    return this->first->data;
}

int DLLStructure::GetTail(){
    return this->last->data;
}

int DLLStructure::GetSize(){
    return this->size;
}

int DLLStructure::GetMax(){
    return this->max;
}

int DLLStructure::GetMin(){
    return this->min;
}


int main (){
    int array[5] = {11,2,7, 22, 4};
    DLLStructure dll(array,5);
    dll.PrintDLL();
    dll.InsertAfter(7,13);
    dll.PrintDLL();
    dll.InsertAfter(25,7);
    dll.PrintDLL();
    dll.InsertBefore(7,26);
    dll.PrintDLL();
    dll.InsertBefore(19,12);
    dll.PrintDLL();
    dll.Delete(22);
    dll.PrintDLL();
    dll.Sort();
    dll.PrintDLL();
    if (dll.IsEmpty()){
        cout << "The list is empty" << endl;
    }

    cout << "Head element is : " << dll.GetHead() << endl;
    cout << "Tail element is : " << dll.GetTail() << endl;
    cout << "Number of elements in the list is : " << dll.GetSize() << endl;
    cout << "Max element is : " << dll.GetMax() << endl;
    cout << "Min element is : " << dll.GetMin() << endl;

    cout << endl << "Question 10" << endl << "If GetSize is called often, we can avoid looping many times by " << endl;
    cout  << "adding size field and defining it when the list is constructed. We update when a node is added or deleted." << endl;
    cout << "We return the field in a constant time " << endl;

    cout << endl << "Question 11" << endl << "If GetMax or GetMin is called very often, " << endl;
    cout << "we can create a max field and a min field and update them whenever an element is added " << endl;
    cout << "or deleted. We simply return the field which will be in a constant time." << endl;
    cout << endl <<"Question 12 " << endl << "It will create a shallow copy." << endl;
    cout << "Therefor the default copy constructor will simply copy the pointer to the DLL, but" << endl;
    cout << "the objects inside will be the same since we didn't create new nodes." << endl ;
    cout << "In my solution. I copy the the pointer of the DLL but also every node inside. Hence, a deep copy." << endl;


    DLLStructure dll2 (dll);
    dll2.PrintDLL();



    return 0;
}
