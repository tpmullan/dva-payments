# Place all the behaviors and hooks related to the matching controller here.
# All this logic will automatically be available in application.js.
# You can use CoffeeScript in this file: http://jashkenas.github.com/coffee-script/
$ ->
  updateFriends = ->
    $.ajax '/friends.json',
      type: 'GET'
      dataType: 'JSON'
      error: (textStatus, errorThrown) ->
        $('#transfer_toAccount').html "AJAX Error: #{textStatus}"
      success: (data, textStatus) ->
        newOptions = []
        $.each data, (i, item) ->
          newOptions.push $("<option>",
            value: item.email
            text: "#{item.name} (#{item.email})"
          )
        $("#transfer_toAccount").html newOptions
        $("#transfer_toAccount").selectmenu("refresh")
  updateFriends()
