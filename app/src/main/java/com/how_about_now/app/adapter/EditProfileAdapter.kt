package com.how_about_now.app.adapter

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.how_about_now.app.R
import com.how_about_now.app.activity.BaseActivity
import com.how_about_now.app.utils.ItemMoveCallback
import kotlinx.android.synthetic.main.adapter_profile.view.*
import java.util.*


class EditProfileAdapter(
    baseActivity: BaseActivity,
    addImageCallBack: AddImageCallBack,
    profileImagesUriArrayList: ArrayList<String>
) :
    RecyclerView.Adapter<EditProfileAdapter.MyViewHolder>(),
    ItemMoveCallback.ItemTouchHelperContract {
    var selectedPosition = 0
    var baseActivity: BaseActivity? = null
    var addImageCallBack: AddImageCallBack? = null
    var profileImagesUriArrayList: ArrayList<String>? = null


    init {
        this.baseActivity = baseActivity
        this.profileImagesUriArrayList = profileImagesUriArrayList
        this.addImageCallBack = addImageCallBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_profile,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (profileImagesUriArrayList!!.size > 0)
            when (position) {
                0 -> {
                    if (profileImagesUriArrayList!!.size > 0) {
                        holder.profileCIV.setImageURI(
                            Uri.parse(
                                profileImagesUriArrayList!!.get(
                                    position
                                )
                            )
                        )
                        holder.addIV.visibility = View.GONE
                        holder.removeIV.visibility = View.VISIBLE
                    } else {
                        holder.addIV.visibility = View.VISIBLE
                        holder.removeIV.visibility = View.GONE
                        holder.profileCIV.setImageResource(R.drawable.ic_default)
                    }
                }
                1 -> {
                    if (profileImagesUriArrayList!!.size > 1) {
                        holder.profileCIV.setImageURI(
                            Uri.parse(
                                profileImagesUriArrayList!!.get(
                                    position
                                )
                            )
                        )
                        holder.addIV.visibility = View.GONE
                        holder.removeIV.visibility = View.VISIBLE
                    } else {
                        holder.addIV.visibility = View.VISIBLE
                        holder.removeIV.visibility = View.GONE
                        holder.profileCIV.setImageResource(R.drawable.ic_default)
                    }
                }
                2 -> {
                    if (profileImagesUriArrayList!!.size > 2) {
                        holder.profileCIV.setImageURI(
                            Uri.parse(
                                profileImagesUriArrayList!!.get(
                                    position
                                )
                            )
                        )
                        holder.addIV.visibility = View.GONE
                        holder.removeIV.visibility = View.VISIBLE
                    } else {
                        holder.addIV.visibility = View.VISIBLE
                        holder.removeIV.visibility = View.GONE
                        holder.profileCIV.setImageResource(R.drawable.ic_default)
                    }
                }
                3 -> {
                    if (profileImagesUriArrayList!!.size > 3) {
                        holder.profileCIV.setImageURI(
                            Uri.parse(
                                profileImagesUriArrayList!!.get(
                                    position
                                )
                            )
                        )
                        holder.addIV.visibility = View.GONE
                        holder.removeIV.visibility = View.VISIBLE
                    } else {
                        holder.addIV.visibility = View.VISIBLE
                        holder.removeIV.visibility = View.GONE
                        holder.profileCIV.setImageResource(R.drawable.ic_default)
                    }
                }
                4 -> {
                    if (profileImagesUriArrayList!!.size > 4) {
                        holder.profileCIV.setImageURI(
                            Uri.parse(
                                profileImagesUriArrayList!!.get(
                                    position
                                )
                            )
                        )
                        holder.addIV.visibility = View.GONE
                        holder.removeIV.visibility = View.VISIBLE
                    } else {
                        holder.addIV.visibility = View.VISIBLE
                        holder.removeIV.visibility = View.GONE
                        holder.profileCIV.setImageResource(R.drawable.ic_default)
                    }
                }
                5 -> {
                    if (profileImagesUriArrayList!!.size > 5) {
                        holder.profileCIV.setImageURI(
                            Uri.parse(
                                profileImagesUriArrayList!!.get(
                                    position
                                )
                            )
                        )
                        holder.addIV.visibility = View.GONE
                        holder.removeIV.visibility = View.VISIBLE
                    } else {
                        holder.addIV.visibility = View.VISIBLE
                        holder.removeIV.visibility = View.GONE
                        holder.profileCIV.setImageResource(R.drawable.ic_default)
                    }
                }
                else -> {
                    holder.addIV.visibility = View.VISIBLE
                    holder.removeIV.visibility = View.GONE
                    holder.profileCIV.setImageResource(R.drawable.ic_default)
                }
            }

        holder.addIV.setOnClickListener(View.OnClickListener {
            if (profileImagesUriArrayList!!.size > 0) {
                if (profileImagesUriArrayList!!.size == holder.adapterPosition) {
                    addImageCallBack?.onAddImageListener(holder.adapterPosition)
                } else {
                    baseActivity!!.showMessage("Please select previous images first.")
                }
            } else {
                addImageCallBack?.onAddImageListener(holder.adapterPosition)
            }
        })

        holder.removeIV.setOnClickListener(View.OnClickListener {
            profileImagesUriArrayList!!.removeAt(holder.adapterPosition)
            holder.profileCIV.setImageURI(null)
            notifyDataSetChanged()
        })
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileCIV = itemView.profileCIV
        val addIV = itemView.addIV
        val removeIV = itemView.removeIV
    }

    interface AddImageCallBack {
        fun onAddImageListener(position: Int)
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(profileImagesUriArrayList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(profileImagesUriArrayList, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(myViewHolder: MyViewHolder) {
        myViewHolder.profileCIV.setBackgroundColor(Color.GRAY)
    }

    override fun onRowClear(myViewHolder: MyViewHolder) {
        myViewHolder.profileCIV.setBackgroundColor(Color.WHITE)
    }

}
