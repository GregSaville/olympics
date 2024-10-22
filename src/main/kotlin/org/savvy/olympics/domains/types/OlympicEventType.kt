package org.savvy.olympics.domains.types

sealed interface OlympicEvent {
    val score: Score
    val rules: List<Rule>
}

data object BeerPong : OlympicEvent {

    fun hitCup(teamRed: Boolean, cupIndex: Int) {
        score.hitCup(teamRed, cupIndex)
        if (score.checkWinner()) {

        }
    }

    fun reverseCup(teamRed: Boolean, cupIndex: Int) {

    }

    override var score: BeerPongScore = BeerPongScore()

    override val rules: List<Rule>
        get() = listOf(
            Rule(
            """
            Players must keep their elbows behind the edge of the table when shooting.
            """.trimIndent()
            ),
            Rule(
            """
            Players may blow or finger the ball out if it is spinning inside the cup.
            """.trimIndent(),
                addendums = listOf(
                    "Men must finger.",
                    "Women must blow.",
                    "Otherwise you're a switch hitter."
                )
            ),
            Rule(
            """
            Players may not damage the table.
            """.trimIndent(),
                addendums = listOf(
                    "Table damage results in an instant lost."
                )
            ),
            Rule(
            """
            Players may rebound their shot.
            """.trimIndent(),
                addendums = listOf(
                    "a player performs a rebound by grabbing their shot before it hits the ground",
                    "a rebound is live until it hits the ground, any other surface is a live shot",
                    "a player may take a second shot upon a successful rebound, the second shot must be a trick shot."
                )
            ),
            Rule(
            """
            Players can call island if a cup is isolated. 
            """.trimIndent(),
                addendums = listOf(
                    "If the player successfully hits the isolated cup, the defensive team must remove the isolated cup along with an additional cup, the offensive team also gets their balls back.",
                    "players get only 2 island calls a game."
                )
            )
        )
}

data object Relay : OlympicEvent {
    override val score: Score
        get() = TODO("Not yet implemented")
    override val rules: List<Rule>
        get() = TODO("Not yet implemented")

}

data object Bags : OlympicEvent {
    override val score: Score
        get() = TODO("Not yet implemented")
    override val rules: List<Rule>
        get() = TODO("Not yet implemented")

}