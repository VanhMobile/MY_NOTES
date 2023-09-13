package com.example.mynotes.entities

class NotesBuilder {
   private var title: String = ""
   private var subTitle: String = ""
   private var dateTime: String = ""
   private var content: String = ""
   private var imgPath: String = ""
   private var webLink: String = ""
   private var noteColor: String = ""

    fun setTitle(title: String) : NotesBuilder{
        this.title = title
        return this
    }

    fun setSubTitle(subTitle: String) : NotesBuilder{
        this.subTitle = subTitle
        return this
    }
    fun setDateTime(dateTime: String) : NotesBuilder{
        this.dateTime = dateTime
        return this
    }

    fun setContent(content: String) : NotesBuilder{
        this.content = content
        return this
    }

    fun setImgPath(imgPath: String) : NotesBuilder{
        this.imgPath = imgPath
        return this
    }

    fun setWebLink(webLink: String) : NotesBuilder{
        this.webLink = webLink
        return this
    }

    fun setNoteColor(noteColor: String) : NotesBuilder{
        this.noteColor = noteColor
        return this
    }

    fun build() :Notes{
        return Notes(null,title,subTitle,dateTime,content, imgPath, webLink, noteColor)
    }

}