package com.laba.valuesconverter

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.laba.valuesconverter.databinding.FragmentFieldsBinding


class FieldsFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var category: Categories
    private var tempCategory = 0
    private var tempToMeasure = 0
    private var tempFromMeasure = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fields, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding: FragmentFieldsBinding = FragmentFieldsBinding.bind(view)
        binding.valueInput.requestFocus()

        viewModel.inputValue.observe(viewLifecycleOwner, Observer {
            binding.valueInput.setText(it)
        })

        viewModel.currentCategory.observe(viewLifecycleOwner, Observer {
            category = it!!
            tempCategory = it.ordinal
            tempFromMeasure = 0
            tempToMeasure = 0
            binding.chooseCategory.setText(it.friendlyNameResId)
            viewModel.postMeasure(0, false)
            viewModel.postMeasure(0, true)
        })

        binding.chooseCategory.setOnClickListener {
            showChooseCategoryDialog()
        }

        binding.toSpinner.setOnClickListener {
            showChooseMeasureDialog(false)
        }

        binding.fromSpinner.setOnClickListener {
            showChooseMeasureDialog(true)
        }

        viewModel.currentFromMeasure.observe(viewLifecycleOwner, Observer {
            binding.fromSpinner.setText(it.friendlyNameResId)
        })

        viewModel.currentToMeasure.observe(viewLifecycleOwner, Observer {
            binding.toSpinner.setText(it.friendlyNameResId)
        })

        viewModel.outputValue.observe(viewLifecycleOwner, Observer {
            binding.convertedValue.setText(it)
        })

        binding.copyInputValue.setOnClickListener {
            val clipboard: ClipboardManager? =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("value", binding.valueInput.text)
            clipboard?.setPrimaryClip(clip)
        }

        binding.copyOutputValue.setOnClickListener {
            val clipboard: ClipboardManager? =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("value", binding.convertedValue.text)
            clipboard?.setPrimaryClip(clip)
        }

        binding.swapButton.setOnClickListener {
            viewModel.swap()
        }
    }

    private fun showChooseCategoryDialog() {
        AlertDialog.Builder(context).setTitle(requireContext().getString(R.string.choose_category))
            .setSingleChoiceItems(Categories.values().map {
                getString(it.friendlyNameResId)
            }.toTypedArray(), viewModel.currentCategory.value?.ordinal ?: 0) { dialog, which ->
                tempCategory = which
            }
            .setPositiveButton("OK") { dialog, which ->
                viewModel.postClear()
                viewModel.postCategory(tempCategory)
            }
            .create()
            .show()
    }

    private fun showChooseMeasureDialog(isFrom: Boolean) {
        AlertDialog.Builder(context).setTitle(requireContext().getString(R.string.choose_category))
            .setSingleChoiceItems(
                Measures.values()
                    .filter { it.category.ordinal == viewModel.currentCategory.value?.ordinal ?: 0 }
                    .map {
                        getString(it.friendlyNameResId)
                    }.toTypedArray(),
                Measures.values()
                    .filter { it.category.ordinal == viewModel.currentCategory.value?.ordinal ?: 0 }
                    .indexOf(
                        (if (isFrom)
                            viewModel.currentFromMeasure
                        else viewModel.currentToMeasure).value
                    )
            ) { dialog, which ->
                if (isFrom) tempFromMeasure = which
                else tempToMeasure = which
            }
            .setPositiveButton("OK") { dialog, which ->
                viewModel.postMeasure(
                    (if (isFrom) tempFromMeasure
                    else tempToMeasure), isFrom
                )
            }
            .create()
            .show()
    }
}
