/* Philippe Bergeron */

#include <iostream>
#include <vector>
#include <cstdlib>
#include <ctime>

using namespace std;

// Ace = 49 (1 in ASCII code)
enum Rank {ACE = 49, TWO, THREE, FOUR, FIVE, SIX, 
            SEVEN, EIGHT, NINE, TEN , JACK = 'J', QUEEN = 'Q', KING = 'K'};
enum Type {CLUBS = 'C', DIAMONDS = 'D', HEARTS = 'H', SPADES = 'S'};

class Card {
    public:
        Rank aRank;
        Type aType;

        // Constructors
        Card(Rank aRank, Type aType);

        // Methods

        int getValue();
        void displayCard();

};
// card constructor
Card::Card(Rank aRank, Type aType){
    this->aRank = aRank;
    this->aType = aType;
}
// value of card based on its ascii code
int Card::getValue(){
    switch (this->aRank){
        case (49) : return 1; break;
        case (50) : return 2; break;
        case (51) : return 3; break;
        case (52) : return 4; break;
        case (53) : return 5; break;
        case (54) : return 6; break;
        case (55) : return 7; break;
        case (56) : return 8; break;
        case (57) : return 9; break;
        case (58) : return 10; break;
        default : return 10;
    }
}
// method to display a card with its rank and type
void Card::displayCard(){
    // if rank is ten we print int 10 since 10 is not in ascii
    if (this->aRank == TEN){
        cout << 10 << (char) this->aType;
    } else {
        cout << (char) this->aRank << (char) this->aType;
    }
}

class Hand{
    public:
        vector<Card> aCards; // vector of cards

    // Methods

        void add(Card c);
        void clear();
        int getTotal();
};

// method to add card to a hand
void Hand::add(Card c){
    this->aCards.push_back(c);
}
// method wich removes all cards from a hand
void Hand::clear(){
    this->aCards.clear();
}

// method which calculates the total sum of numerical value of a hand
// and which is in the player's best interest
int Hand::getTotal(){
    int total = 0;
    int acesNb = this->aCards.size();
    // calculates sum of cards which are NOT aces
    // calculates the number of aces in hand
    for (int i = 0; i<this->aCards.size();i++){
        if (this->aCards[i].getValue() != 1){
            total += this->aCards[i].getValue();
            acesNb--;
        }
        
    }
    // for every ace card
    // add 11 if possible
    // else add 1
    while (acesNb != 0){
        if (total + 11  <= 21){
            total += 11;
        } else {
            total += 1;
        }
        acesNb--;
    }

    return total;
}
class Deck{
    public:
        vector<Card> aCards;
    
        // Methods

        void Populate();
        void shuffle();
        Card deal();
};
// Initializes all 52 cards and adds them to deck
void Deck::Populate(){
    Type t = CLUBS;
    for (int rank = ACE; rank!=59;rank++){
        this->aCards.push_back(Card((Rank) rank,t));
    }
    this->aCards.push_back(Card(JACK,t));
    this->aCards.push_back(Card(QUEEN,t));
    this->aCards.push_back(Card(KING,t));

    t = DIAMONDS;
    for (int rank = ACE; rank!=59;rank++){
        this->aCards.push_back(Card((Rank) rank,t));
    }
    this->aCards.push_back(Card(JACK,t));
    this->aCards.push_back(Card(QUEEN,t));
    this->aCards.push_back(Card(KING,t));

    t = HEARTS;
    for (int rank = ACE; rank!=59;rank++){
        this->aCards.push_back(Card((Rank) rank,t));
    }
    this->aCards.push_back(Card(JACK,t));
    this->aCards.push_back(Card(QUEEN,t));
    this->aCards.push_back(Card(KING,t));
    t = SPADES;
    for (int rank = ACE; rank!=59;rank++){
        this->aCards.push_back(Card((Rank) rank,t));
    }
    this->aCards.push_back(Card(JACK,t));
    this->aCards.push_back(Card(QUEEN,t));
    this->aCards.push_back(Card(KING,t));
}
// shuffles all cards in the deck randomly
void Deck::shuffle(){
    
    static int ra = 0;
    srand(ra+time(NULL));
    for (int i = 0; i<this->aCards.size();i++){
        int k = i + rand() % (this->aCards.size() - i);
        Card a = aCards[i];
        Card b = aCards[k]; 
        this->aCards.erase(this->aCards.begin() + i);
        this->aCards.insert(this->aCards.begin()+i,b);
        this->aCards.erase(this->aCards.begin() + k);
        this->aCards.insert(this->aCards.begin()+k,a);
    }
    ra++;
}
// pops the last card of the vector
Card Deck::deal(){
    Card next = aCards.back();
    this->aCards.erase(this->aCards.begin() + this->aCards.size()-1);
    return  next;
}

class AbstractPlayer{
    public:
        Hand aHand; // Every player has a hand
        virtual bool isDrawing() const = 0;
        // busted if total is > 21
        bool isBusted(){
            return (this->aHand.getTotal() > 21);
        }

};


class HumanPlayer : public AbstractPlayer {
    public:
        HumanPlayer(){}
        // takes a char input from human to decide to draw
        bool isDrawing() const{
            char answer;
            cout << "Do you want to draw? (y/n): ";
            cin >> answer;
            return (answer == 'y' ? true : false);
        }
        void announce(string s);
};
// method that announces the outcome of game
// Essentialy prints a string
void HumanPlayer::announce(string s){
    cout << s << endl;
}



// field that decides if computer player draws
class ComputerPlayer : public AbstractPlayer {
    public:
        ComputerPlayer(){this->draw = true;}
        bool draw;
    
        bool isDrawing() const{
            return draw;
        }
        
};


class BlackJackGame{
    public :
        Deck m_deck;
        ComputerPlayer m_casino;
        HumanPlayer player1;
         
         void play();

};

void BlackJackGame::play(){
    // Intialize and shuffle deck
    m_deck.Populate();
    m_deck.shuffle();
    
    // Deal 1 card to CPU and 2 to human
    m_casino.aHand.add(m_deck.deal());

    player1.aHand.add(m_deck.deal());
    player1.aHand.add(m_deck.deal());

    // Prints cards to screen
    cout << "Casino: ";
    m_casino.aHand.aCards[0].displayCard();
    cout << " [" << m_casino.aHand.getTotal() << ']' << endl;

    cout << "Player: ";
    player1.aHand.aCards[0].displayCard();
    cout << " ";
    player1.aHand.aCards[1].displayCard();
    cout << " [" << player1.aHand.getTotal() << ']' << endl;

    // asks human to draw
    // if yes, deals a card and checks if busted
    bool drawPlay = player1.isDrawing();
    while (drawPlay){
        player1.aHand.add(m_deck.deal());
        cout << "Player:";
        for (int i = 0; i<player1.aHand.aCards.size();i++){
            cout << " ";
            player1.aHand.aCards[i].displayCard();
        }
        cout << " [" << player1.aHand.getTotal() << ']' << endl;
        // announce bust
        if (player1.isBusted()){
            player1.announce("Player busts.");
            player1.announce("Casino Wins.");
            break;
        }
        drawPlay = player1.isDrawing();
    }
    // quit current round if busted
    if  (player1.isBusted()){
        m_deck.aCards.clear();
        player1.aHand.clear();
        m_casino.aHand.clear();
    } else {
        // casino draws if hand total samller than 17
        m_casino.draw = m_casino.aHand.getTotal() <= 16;
        bool drawComp = m_casino.isDrawing();
        while (drawComp){
            m_casino.aHand.add(m_deck.deal());
            cout << "Casino:";
            for (int i = 0; i<m_casino.aHand.aCards.size();i++){
                cout << " ";
                m_casino.aHand.aCards[i].displayCard();
                }
            cout << " [" << m_casino.aHand.getTotal() << ']' << endl;
            m_casino.draw = m_casino.aHand.getTotal() <= 16;
            drawComp = m_casino.isDrawing();
            }
        // player wins if casino busts
        if (m_casino.isBusted()){
            player1.announce("Player wins.");
            player1.announce("Casino busts.");
            m_deck.aCards.clear();
            player1.aHand.clear();
            m_casino.aHand.clear();
        } else {
            // Player's total is greater -> wins
            if (player1.aHand.getTotal() > m_casino.aHand.getTotal()){
                player1.announce("Player wins.");
                m_deck.aCards.clear();
                player1.aHand.clear();
                m_casino.aHand.clear();
            // Equality -> push no one wins
            } else if (player1.aHand.getTotal() == m_casino.aHand.getTotal()){
                player1.announce("Push: No one wins.");
                m_deck.aCards.clear();
                player1.aHand.clear();
                m_casino.aHand.clear();
            // Casino's total is greater -> player loses
            } else {
                player1.announce("Casino wins.");
                m_deck.aCards.clear();
                player1.aHand.clear();
                m_casino.aHand.clear();
            }
        }
    }
    
}

int main(){
    

    cout << "\tWelcome to the Comp322 Blackjack Game!" << endl << endl;

    BlackJackGame game;

    // The main loop of the game
    bool playAgain = true;
    char answer = 'y';
    while (playAgain){
        game.play();

        // Check wether the player would like to play another round
        cout << "Would you like another round? (y/n): ";
        cin >> answer;
        cout << endl << endl;
        playAgain = (answer == 'y' ? true : false);
    }

    cout << "Game over!";
    return 0;
}