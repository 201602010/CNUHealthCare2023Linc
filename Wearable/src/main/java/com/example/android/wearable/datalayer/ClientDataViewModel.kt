package com.example.android.wearable.datalayer

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.example.android.wearable.datalayer.constants.*
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent

class ClientDataViewModel(
    application: Application
) :
    AndroidViewModel(application),
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {

    private val _events = mutableStateListOf<Event>()

    /**
     * The list of events from the clients.
     */
    val events: List<Event> = _events

    @SuppressLint("VisibleForTests")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        // Add all events to the event log
        mDATA = "$mHeartRate," + String.format("%.2f,%.2f,%.2f,%.2f,%.2f,%.2f", mGravAccelerateX,mGravAccelerateX,mGravAccelerateX,mLinAccelerateX,mLinAccelerateY,mLinAccelerateZ)
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
                        //text = dataEvent.dataItem.toString()
                        text = mDATA
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
                        //text = dataEvent.dataItem.toString()
                        text = mDATA
                    )
                )
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if(_events.find {it.title.equals(R.string.message) } == null){
            _events.add(
                Event(
                    title = R.string.message,
                    text = messageEvent.toString()
                )
            )
        }
        else {
            _events.set(_events.indexOfLast { it.title.equals(R.string.message)},
                Event(
                    title = R.string.message,
                    text = messageEvent.toString()
                )
            )
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
    val text: String
)
