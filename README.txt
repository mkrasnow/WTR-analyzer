1) If you are simply looking to download and use the application, all you need is to download and run the file 'WTR_analyzer2.jar' located in WTR-analyzer/WTR_analyzer2/dist/WTR_analyzer2.jar.  You need Java installed to run the jar file.
2) Data files must be formatted in a specific way:
	a) They must be csv format
	b) The file can contain multiple scales per subject.  Scales are organized left to right, subjects are rows
	c) For each scale, the header is:
		Player 1:
		Player 2:
		Scale Label:
		Number of Choices
		Choice #
		ChoosePlayer1- Player1Gets
		ChoosePlayer1- Player2Gets
		ChoosePlayer2- Player1Gets
		ChoosePlayer2- Player2Gets
		[empty row]
	d) Choices are numbered 1 to n in any order, each in their own column
	e) Fill in the values of who gets what for each choice in the appropriate cell
	f) The next scale repeates those row labels in the column immediately after the last choice in the preceding scale
	g) The next row only contains the word 'Subject' in the first cell
	h) The remaining rows start with a subject id (number or string) and then the subject's choice for each questions.  1=choosing 'player 1', 2 =choosing 'player 2'

See the sample file if you have any questions
