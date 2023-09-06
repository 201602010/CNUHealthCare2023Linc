package com.example.android.wearable.datalayer

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * A state holder for the client data.
 */
class ClientDataViewModel :
    ViewModel(),
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {

    private val _events = mutableStateListOf<Event>()

    private lateinit var database: DatabaseReference

    /**
     * The list of events from the clients.
     */
    val events: List<Event> = _events

    @SuppressLint("VisibleForTests")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        if(_events.isEmpty()) {
            _events.addAll(
                dataEvents.map { dataEvent ->
                    val title = when (dataEvent.type) {
                        DataEvent.TYPE_CHANGED -> R.string.data_item_changed
                        DataEvent.TYPE_DELETED -> R.string.data_item_deleted
                        else -> R.string.data_item_unknown
                    }

                    Event(
                        title = title,
                        text = dataEvent.dataItem.toString()
                    )
                }
            )
        }
        else {
            dataEvents.map { dataEvent ->
                val title = when (dataEvent.type) {
                    DataEvent.TYPE_CHANGED -> R.string.data_item_changed
                    DataEvent.TYPE_DELETED -> R.string.data_item_deleted
                    else -> R.string.data_item_unknown
                }
                _events.set(_events.indexOfLast { it.title.equals(title) },
                    Event(
                        title = title,
                        text = dataEvent.dataItem.toString()
                    )
                )
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if(_events.find {it.title.equals(R.string.message_from_watch) } == null){
            _events.add(
                Event(
                    title = R.string.message_from_watch,
                    text = messageEvent.toString()
                )
            )
        }
        else {
            _events.set(_events.indexOfLast { it.title.equals(R.string.message_from_watch)},
                Event(
                    title = R.string.message_from_watch,
                    text = messageEvent.toString()
                )
            )
        }
        var arr = messageEvent.path.split(",", limit=2)
        database = Firebase.database.reference
        //database.child(arr[0]).push().setValue(messageEvent.path)
        if(arr[1] != "") {
            database.child(arr[0]).push().setValue(arr[1])
        }
    }

    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {
        _events.add(
            Event(
                title = R.string.capability_changed,
                text = capabilityInfo.toString()
            )
        )
    }
}

/**
 * A data holder describing a client event.
 */
data class Event(
    @StringRes val title: Int,
    var text: String
)
