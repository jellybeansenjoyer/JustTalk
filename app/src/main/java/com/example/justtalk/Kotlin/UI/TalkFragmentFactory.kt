package com.example.justtalk.Kotlin.UI

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.justtalk.Kotlin.models.User

class TalkFragmentFactory(private val user: User) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        when(className){
            ChatFragment::class.java.name->return ChatFragment(user)
            TalkFragment::class.java.name-> return TalkFragment(user)
            InfoFragment::class.java.name->return InfoFragment(user)
            else ->  return super.instantiate(classLoader, className)

        }
    }
}