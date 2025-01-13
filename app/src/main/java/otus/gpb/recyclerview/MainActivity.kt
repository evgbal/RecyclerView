package otus.gpb.recyclerview

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private lateinit var chatAdapter: ChatAdapter
    private var itemTouchHelper: ItemTouchHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycler_view_devider)!!)
        recyclerView.addItemDecoration(divider)

        chatAdapter = ChatAdapter(
//            onDeleteChat = { chat ->
//                val updatedList = chatAdapter.currentList.toMutableList()
//                updatedList.remove(chat)
//                chatAdapter.submitList(updatedList)
//            },
            onArchiveChat = { chat ->
                // Обработка архивирования
                val updatedList = chatAdapter.currentList.toMutableList()
                updatedList.remove(chat)
                chatAdapter.submitList(updatedList)
                Toast.makeText(this, "$chat archived", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chatAdapter


//        // Set up ItemTouchHelper for swipe actions
//        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
//            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
//            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
//        ) {
//            override fun onMove(
//                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
//            ): Boolean = false
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
//                //val chat = chatAdapter.getItem(position)
//                // Handle swipe action, e.g., delete chat
//                //chatAdapter.onDeleteChat(chat)
//
//
//                //val itemLayout = viewHolder.itemView.findViewById<LinearLayout>(R.id.chatItemLayout)
//                val archiveLayout = viewHolder.itemView.findViewById<FrameLayout>(R.id.archiveButtonLayout)
//
//                // Показать шторку с архивом при свайпе
//                archiveLayout.visibility = View.VISIBLE
//
//                // Сдвигаем основной элемент (chatItemLayout) влево, чтобы показать архив
//                //itemLayout.animate().translationX(-itemLayout.width.toFloat()).setDuration(200).start()
//
//                // Кнопка архивирования
//                val archiveButton = viewHolder.itemView.findViewById<ImageButton>(R.id.archiveButton)
//                archiveButton.setOnClickListener {
//                    // Обработать архивирование
//                    //onArchiveChat(chat)
//                    archiveLayout.visibility = View.GONE  // Скрыть архивный элемент
//                }
//            }
//
//            override fun isItemViewSwipeEnabled(): Boolean = true
//        }

        //val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        //itemTouchHelper.attachToRecyclerView(recyclerView)

        val itemTouchHelperCallback = object : ItemTouchHelper.Callback() {

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                // Позволяем свайпы влево
                return makeMovementFlags(0, ItemTouchHelper.LEFT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Перемещение элементов отключено
                return false
            }

//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                itemTouchHelper?.attachToRecyclerView(null)
//                itemTouchHelper?.attachToRecyclerView(recyclerView)
//
//                val archiveLayout = viewHolder.itemView.findViewById<FrameLayout>(R.id.archiveButtonLayout)
//                val itemLayout = viewHolder.itemView.findViewById<View>(R.id.chatItemLayout)
//
//                val position = viewHolder.adapterPosition
//                if (position == RecyclerView.NO_POSITION) return
//
//                if (direction == ItemTouchHelper.LEFT && archiveLayout.visibility != View.VISIBLE) {
//
//
//                    // Проверяем расстояние свайпа
//                    val translationX = abs(viewHolder.itemView.translationX);
//                    if (translationX >= viewHolder.itemView.width * 0.8) {
//
//                        // Длинный свайп: Удаление
//                        //val position = viewHolder.adapterPosition
//                        val chatObj = chatAdapter.currentList[position];
//                        val updatedList = chatAdapter.currentList.toMutableList()
//                        updatedList.remove(chatObj)
//                        chatAdapter.submitList(updatedList)
//                        //val item: String = chatAdapter.getData().get(position)
//                        //chatAdapter.removeItem(position)
//                        Toast.makeText(this@MainActivity, "Deleted: $chatObj", Toast.LENGTH_SHORT).show()
//                        chatAdapter.notifyItemRemoved(position)
//                    } else {
//                        // Показать шторку
//                        archiveLayout.visibility = View.VISIBLE
//                        itemLayout.animate().translationX(-archiveLayout.width.toFloat()).setDuration(500).start()
//                    }
//
//                    // Сбросить состояние ViewHolder после анимации
//                    //itemLayout.postDelayed({
//                        //itemTouchHelper.attachToRecyclerView(null)
//                        //chatAdapter.notifyItemChanged(position)
//                        //itemTouchHelper.attachToRecyclerView(recyclerView)
//                    //}, 500)
//                }
//
////                // Сброс состояния
////                itemLayout.postDelayed({
////                    archiveLayout.visibility = View.GONE
////                    itemLayout.animate().translationX(0f).setDuration(1000).start()
////                    chatAdapter.notifyItemChanged(position)
////                }, 3000)
//            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) return

                if (direction == ItemTouchHelper.LEFT) {
                    // Длинный свайп (удаление)
                    val updatedList = chatAdapter.currentList.toMutableList()
                    updatedList.removeAt(position)
                    chatAdapter.submitList(updatedList)
                    Toast.makeText(this@MainActivity, "Deleted item at position $position", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView

                // Получаем смещение dX (оно будет отрицательным для свайпа влево)
                val translationX = abs(dX)

                // Устанавливаем шторку в видимое состояние, если свайп достаточно длинный
                val archiveLayout = itemView.findViewById<FrameLayout>(R.id.archiveButtonLayout)
                if (translationX >= itemView.width * 0.8f) {
                    // Длинный свайп (удаление)
                    archiveLayout.visibility = View.GONE
                } else {
                    // Полусвайп (показ шторки)
                    archiveLayout.visibility = View.VISIBLE

                }

                // Выполняем стандартный рендеринг
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }


//            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
//                super.clearView(recyclerView, viewHolder)
//                // Возвращаем элемент в исходное положение
//                val archiveLayout = viewHolder.itemView.findViewById<FrameLayout>(R.id.archiveButtonLayout)
//                val itemLayout = viewHolder.itemView.findViewById<View>(R.id.chatItemLayout)
//                //archiveLayout.visibility = View.GONE
//                // Скрываем шторку и сбрасываем позицию
//                if (archiveLayout.visibility != View.GONE) {
//                    archiveLayout.visibility = View.GONE
//                    //     itemLayout.translationX = 0f
//                    itemLayout.animate().translationX(0f).setDuration(500).start()
//
//                    val position = viewHolder.adapterPosition
//                    // Сбросить состояние ViewHolder после анимации
//                    itemLayout.postDelayed({
//                        //itemTouchHelper.attachToRecyclerView(null)
//                        chatAdapter.notifyItemChanged(position)
//                        //itemTouchHelper.attachToRecyclerView(recyclerView)
//                    }, 500)
//
//
//                    //val position = viewHolder.adapterPosition
//                    //chatAdapter.notifyItemChanged(position)
//                }
//
//            }


            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)

                val archiveLayout = viewHolder.itemView.findViewById<FrameLayout>(R.id.archiveButtonLayout)
                val itemLayout = viewHolder.itemView.findViewById<View>(R.id.chatItemLayout)

                // Проверяем, видима ли шторка
                if (archiveLayout.visibility != View.GONE) {
                    // Анимация возврата в исходное положение
                    // Сброс состояния
                    itemLayout.postDelayed({
                        itemLayout.animate()
                            .translationX(0f)
                            .setDuration(500)
                            .withEndAction {
                                // После завершения анимации скрываем шторку
                                archiveLayout.visibility = View.GONE

                                // Обновляем элемент только после завершения анимации
                                val position = viewHolder.adapterPosition
                                if (position != RecyclerView.NO_POSITION) {
                                    chatAdapter.notifyItemChanged(position)
                                }
                            }
                            .start()
                    }, 3000)

                }
            }

            override fun isItemViewSwipeEnabled(): Boolean = true
        }

        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper?.attachToRecyclerView(recyclerView)

        // Загрузить данные
        chatAdapter.submitList(loadChats())
    }

}


private fun loadChats(): List<Chat> {
    return listOf(
        UserChat(
            id = 1,
            lastUserName = "Alice",
            lastMessage = "Hi there!",
            time = "12:30",
            isVerified = true,
            avatarUrl = "https://gravatar.com/avatar/478f6b94ddcde12416b90f22d7588cab?s=400&d=robohash&r=x",
            isScam = false,
            unreadMessageCount = 0,
            isMute = false,
            isSpeeching = false,
            isTyping = false,
            isUnreadedAnswerToYou = false,
            isPinned = true,
            isOpponnentReaded = false,
            isAnswered = true
        ),
        GroupChat(
            id = 2,
            title = "Group topic",
            lastUserName = "Bob",
            lastMessage = "How are you?",
            time = "11:15",
            isVerified = false,
            avatarUrl = "https://gravatar.com/avatar/1ae9a8dbb50baebe44d7b96d012a73d5?s=400&d=robohash&r=x",
            isScam = true,
            unreadMessageCount = 0,
            isMute = false,
            isSpeeching = false,
            isTyping = false,
            isUnreadedAnswerToYou = true,
            isAnswered = true,
            isLock = true,
            lastAnswererUrl = "https://i.pinimg.com/736x/c9/fe/fb/c9fefb489d5fdc792ae324103255edd2.jpg"

        ),
        UserChat(
            id = 3,
            lastUserName = "Charlie",
            lastMessage = "Meeting at 3?",
            time = "Yesterday",
            isVerified = true,
            avatarUrl = "https://gravatar.com/avatar/f07d785a9ee32506a7a077f25466f98b?s=400&d=robohash&r=x",
            isScam = false,
            unreadMessageCount = 11,
            isMute = true,
            isSpeeching = true,
            isTyping = false,
            isUnreadedAnswerToYou = true
        ),
        UserChat(
            id = 4,
            lastUserName = "Marilyn",
            lastMessage = "Tomorow?",
            time = "Yesterday",
            isVerified = false,
            avatarUrl = "https://i.pinimg.com/736x/75/26/5b/75265b439fb335a418e79b13b447b1a6.jpg",
            isScam = false,
            unreadMessageCount = 1,
            isMute = false,
            isSpeeching = false,
            isTyping = true,
            isUnreadedAnswerToYou = true,
            isAnswered = false,
            isOpponnentReaded = false
        ),
        UserChat(
            id = 4,
            lastUserName = "Elvis",
            lastMessage = "Dear! Not today.",
            time = "Yesterday",
            isVerified = true,
            avatarUrl = "https://www.kino-teatr.ru/news/20390/183753.jpg",
            isScam = false,
            unreadMessageCount = 0,
            isMute = false,
            isSpeeching = false,
            isTyping = false,
            isUnreadedAnswerToYou = false,
            isAnswered = true,
            isOpponnentReaded = true
        )


    )
}