package otus.gpb.recyclerview

// Parent interface
interface Chat {
    val id: Int
    val lastUserName: String
    val lastMessage: String
    val time: String
    val isVerified: Boolean
    val isScam: Boolean
    val unreadMessageCount: Int
    val avatarUrl: String?
    val lastAnswererUrl: String?
    val isPinned: Boolean
    var isMute: Boolean
    var isSpeeching: Boolean
    var isTyping: Boolean
    var isUnreadedAnswerToYou: Boolean
    var isAnswered: Boolean
    var isOpponnentReaded: Boolean
    var isLock: Boolean

    override fun equals(other: Any?): Boolean
}

// Child class for group chat
data class GroupChat(
    override val id: Int,
    val title: String,
    override val lastUserName: String,
    override val lastMessage: String,
    override val time: String,
    override val isVerified: Boolean = true,
    override val isScam: Boolean = false,
    override val unreadMessageCount: Int = 0,
    override val avatarUrl: String? = null,
    override val lastAnswererUrl: String? = null,
    override val isPinned: Boolean = false,
    override var isMute: Boolean = false,
    override var isSpeeching: Boolean = false,
    override var isTyping: Boolean = false,
    override var isUnreadedAnswerToYou: Boolean = false,
    override var isAnswered: Boolean = false,
    override var isOpponnentReaded: Boolean = false,
    override var isLock: Boolean = false


) : Chat {

    // Custom equals implementation for GroupChat
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GroupChat) return false
        return id == other.id &&
                lastUserName == other.lastUserName &&
                lastMessage == other.lastMessage &&
                time == other.time &&
                isVerified == other.isVerified &&
                isScam == other.isScam &&
                unreadMessageCount == other.unreadMessageCount &&
                avatarUrl == other.avatarUrl &&
                lastAnswererUrl == other.lastAnswererUrl &&
                isPinned == other.isPinned &&
                isMute == other.isMute &&
                isSpeeching == other.isSpeeching &&
                isTyping == other.isTyping &&
                isUnreadedAnswerToYou == other.isUnreadedAnswerToYou &&
                isAnswered == other.isAnswered &&
                isOpponnentReaded == other.isOpponnentReaded &&
                isLock == other.isLock
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

// Child class for user chat
data class UserChat(
    override val id: Int,
    override val lastUserName: String,
    override val lastMessage: String,
    override val time: String,
    override val isVerified: Boolean = true,
    override val isScam: Boolean = false,
    override val unreadMessageCount: Int = 0,
    override val avatarUrl: String? = null,
    override val lastAnswererUrl: String? = null,
    override val isPinned: Boolean = false,
    override var isMute: Boolean = false,
    override var isSpeeching: Boolean = false,
    override var isTyping: Boolean = false,
    override var isUnreadedAnswerToYou: Boolean = false,
    override var isAnswered: Boolean = false,
    override var isOpponnentReaded: Boolean = false,
    override var isLock: Boolean = false
) : Chat {

    // Custom equals implementation for UserChat
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserChat) return false
        return id == other.id &&
                lastUserName == other.lastUserName &&
                lastMessage == other.lastMessage &&
                time == other.time &&
                isVerified == other.isVerified &&
                isScam == other.isScam &&
                unreadMessageCount == other.unreadMessageCount &&
                avatarUrl == other.avatarUrl &&
                lastAnswererUrl == other.lastAnswererUrl &&
                isPinned == other.isPinned &&
                isMute == other.isMute &&
                isSpeeching == other.isSpeeching &&
                isTyping == other.isTyping &&
                isUnreadedAnswerToYou == other.isUnreadedAnswerToYou &&
                isAnswered == other.isAnswered &&
                isOpponnentReaded == other.isOpponnentReaded &&
                isLock == other.isLock
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

