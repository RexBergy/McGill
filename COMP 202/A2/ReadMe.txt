Hi!:)

Here are some problems I encountered:

	1. In text_to_braille module, there is a big error concerning file_to_braille. 
	This error appears "TypeError: can only concatenate str (not "NoneType") to str"
 	"s += word_to_braille(word)" (line 235) in paragraph_to_braille appears to be the problem
	It is fixed by typing " s += str(word_to_braille(word))"
	This error causes the files not to be translated, so file_diff doesn't work
###

	2. In english_to_braille, function "english_num(text)" line 166, if you remove the '#' 
	from the doctring, a lot of errors appear which I don"t understand.
###

	3. I didn't know how to properly convert english numbers but I tried (english_num(text))

Thank you for understanding!
Good day