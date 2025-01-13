package otus.gpb.recyclerview

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private lateinit var chatAdapter: ChatAdapter
    private var itemTouchHelper: ItemTouchHelper? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        this.title = getString(R.string.app_title)

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toolbar = findViewById(R.id.toolbar)

        // Set up toolbar
        setSupportActionBar(toolbar)

        // Add navigation drawer toggle
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.colorOnPrimary)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle navigation menu item clicks
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, getString(R.string.home_selected), Toast.LENGTH_SHORT).show()
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, getString(R.string.settings_selected), Toast.LENGTH_SHORT).show()
                }
                R.id.nav_about -> {
                    Toast.makeText(this,getString(R.string.about_selected), Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }


        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycler_view_devider)!!)
        recyclerView.addItemDecoration(divider)

        chatAdapter = ChatAdapter(
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
            title = "Mentors",
            lastUserName = "Bob",
            lastMessage = "How are you?",
            time = "11:15",
            isVerified = false,
            avatarUrl = "https://gravatar.com/avatar/1ae9a8dbb50baebe44d7b96d012a73d5?s=400&d=robohash&r=x",
            isScam = false,
            unreadMessageCount = 7,
            isMute = false,
            isSpeeching = false,
            isTyping = false,
            isUnreadedAnswerToYou = true,
            isAnswered = true,
            isLock = false,
            isPinned = true,
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
            unreadMessageCount = 15,
            isMute = false,
            isSpeeching = false,
            isTyping = true,
            isUnreadedAnswerToYou = false,
            isAnswered = false,
            isOpponnentReaded = false
        ),
        UserChat(
            id = 5,
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
        ),
        GroupChat(
            id = 6,
            title = "Cat's and mouse",
            lastUserName = "Catzilla",
            lastMessage = "Good morning!",
            time = "11:15",
            isVerified = false,
            avatarUrl = "https://s9.travelask.ru/uploads/post/000/031/157/main_image/facebook-bf918145c8d2cee688d53ee7112500d3.jpg",
            isScam = true,
            unreadMessageCount = 0,
            isMute = false,
            isSpeeching = false,
            isTyping = false,
            isUnreadedAnswerToYou = true,
            isAnswered = true,
            isLock = true,
            lastAnswererUrl = "https://i.pinimg.com/originals/52/c6/65/52c665df0515dd447eb92544374cf543.jpg"

        ),
        GroupChat(
            id = 7,
            title = "StarLine Tours",
            lastUserName = "Bear Grils",
            lastMessage = "Let's go!",
            time = "11:15",
            isVerified = false,
            avatarUrl = "https://avatars.mds.yandex.net/i?id=c7269ba0c5fc64e968daedd67f497d1d82453fcf-7760894-images-thumbs&n=13",
            isScam = false,
            unreadMessageCount = 101,
            isMute = false,
            isSpeeching = false,
            isTyping = false,
            isUnreadedAnswerToYou = false,
            isAnswered = true,
            isLock = false,
            lastAnswererUrl = "https://avatars.mds.yandex.net/get-kinopoisk-image/1898899/78473e64-0a54-46ba-87ad-94b2822e9aaf/1920x"

        )


    )
}