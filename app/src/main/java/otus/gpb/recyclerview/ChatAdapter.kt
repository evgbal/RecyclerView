package otus.gpb.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import otus.gpb.recyclerview.databinding.ItemChatBinding


class ChatAdapter(
    //private val onDeleteChat: (Chat) -> Unit,
    private val onArchiveChat: (Chat) -> Unit // Новый callback для архивирования
) : ListAdapter<Chat, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    inner class ChatViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {
            // Установка данных в зависимости от типа чата
            when (chat) {
                is GroupChat -> {
                    binding.chatTopic.text = chat.title
                    binding.chatLastUser.text = chat.lastUserName
                    binding.chatLastUser.visibility = View.VISIBLE
                }
                is UserChat -> {
                    binding.chatTopic.text = chat.lastUserName
                    binding.chatLastUser.visibility = View.GONE
                }
            }

            binding.chatLastMessage.text = chat.lastMessage
            binding.chatTime.text = chat.time

            // Отображение иконок статусов
            binding.speechingStatusIcon.visibility = if (chat.isSpeeching) View.VISIBLE else View.GONE
            binding.muteChannelIcon.visibility = if (chat.isMute) View.VISIBLE else View.GONE

            binding.lockIcon.visibility = if (chat.isLock) View.VISIBLE else View.GONE
            binding.scamIcon.visibility = if (chat.isScam) View.VISIBLE else View.GONE

            binding.verifiedIcon.visibility = if (chat.isVerified) View.VISIBLE else View.GONE

            binding.unreadCount.visibility = if (chat.unreadMessageCount > 0) View.VISIBLE else View.GONE
            chat.unreadMessageCount.toString().also { binding.unreadCount.text = it }

            binding.typingIcon.visibility = if (chat.isTyping) View.VISIBLE else View.GONE
            binding.typing.visibility = if (chat.isTyping) View.VISIBLE else View.GONE
            binding.chatLastMessage.visibility = if (chat.isTyping) View.GONE else View.VISIBLE

            binding.answeredToYouStatus.visibility = if (chat.isUnreadedAnswerToYou) View.VISIBLE else View.GONE

            binding.answeredIcon.visibility = if (chat.isAnswered && !chat.isOpponnentReaded) View.VISIBLE else View.GONE
            binding.answeredAndReadedIcon.visibility = if (chat.isAnswered && chat.isOpponnentReaded) View.VISIBLE else View.GONE
            binding.pinnedStatus.visibility = if (chat.isPinned) View.VISIBLE else View.GONE

            val targetSize = 500 // Размер в пикселях, до которого нужно увеличить изображение
            // Загрузка аватара
            if (chat.avatarUrl != null) {
                Glide.with(binding.chatAvatar.context)
                    .load(chat.avatarUrl)
                    .transform(ResizeAndCropTransformation(targetSize), CircleCrop()) // Обрезаем и применяем круг
                    .into(binding.chatAvatar)
            } else {
                binding.chatAvatar.setImageResource(R.drawable.ic_placeholder_avatar)
            }

            // Загрузка аватара ответившего
            if (chat.lastAnswererUrl != null) {
                Glide.with(binding.answererIcon.context)
                    .load(chat.lastAnswererUrl)
                    //.circleCrop()
                    .into(binding.answererIcon)
                binding.answererIcon.visibility = View.VISIBLE
            } else {
                binding.answererIcon.setImageResource(android.R.color.transparent)
                binding.answererIcon.visibility = View.GONE
            }


//            // Обработчик длинного нажатия для удаления
//            binding.root.setOnLongClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    val chatObj = currentList[position]
//                    onDeleteChat(chatObj)
//                }
//                true
//            }

            // Обработчик нажатия на кнопку архивирования
            binding.archiveButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val chatObj = currentList[position]
                    // Обработка архивации
                    onArchiveChat(chatObj)
                    // Скрыть шторку
                    val archiveLayout = binding.root.findViewById<FrameLayout>(R.id.archiveButtonLayout)
                    archiveLayout.visibility = View.GONE
                    notifyItemChanged(position)
                }
            }


            binding.root.setOnClickListener {
                val archiveLayout = binding.root.findViewById<FrameLayout>(R.id.archiveButtonLayout)
                val itemLayout = binding.root.findViewById<View>(R.id.chatItemLayout)
                if (archiveLayout.visibility == View.VISIBLE) {
                    //archiveLayout.visibility = View.GONE
                    //notifyItemChanged(adapterPosition)

                    // Анимация возврата в исходное положение
                    itemLayout.animate()
                        .translationX(0f)
                        .setDuration(500)
                        .withEndAction {
                            // После завершения анимации скрываем шторку
                            archiveLayout.visibility = View.GONE
                            // Обновляем элемент только после завершения анимации
                            val position = adapterPosition
                            if (position != RecyclerView.NO_POSITION) {
                                notifyItemChanged(position)
                            }
                        }
                        .start()

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatBinding.inflate(inflater, parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<Chat>() {
    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean = oldItem == newItem
}
