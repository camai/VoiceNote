package com.jg.voicenote.core.database.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.jg.voicenote.core.database.converter.DateTimeConverter
import com.jg.voicenote.core.database.entity.VoiceRecordEntity
import java.time.LocalDateTime
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class VoiceRecordDao_Impl(
  __db: RoomDatabase,
) : VoiceRecordDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfVoiceRecordEntity: EntityInsertAdapter<VoiceRecordEntity>

  private val __dateTimeConverter: DateTimeConverter = DateTimeConverter()

  private val __deleteAdapterOfVoiceRecordEntity: EntityDeleteOrUpdateAdapter<VoiceRecordEntity>

  private val __updateAdapterOfVoiceRecordEntity: EntityDeleteOrUpdateAdapter<VoiceRecordEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfVoiceRecordEntity = object : EntityInsertAdapter<VoiceRecordEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `voice_records` (`id`,`title`,`filePath`,`duration`,`fileSize`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: VoiceRecordEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.filePath)
        statement.bindLong(4, entity.duration)
        statement.bindLong(5, entity.fileSize)
        val _tmp: String? = __dateTimeConverter.fromLocalDateTime(entity.createdAt)
        if (_tmp == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmp)
        }
      }
    }
    this.__deleteAdapterOfVoiceRecordEntity = object :
        EntityDeleteOrUpdateAdapter<VoiceRecordEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `voice_records` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: VoiceRecordEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfVoiceRecordEntity = object :
        EntityDeleteOrUpdateAdapter<VoiceRecordEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `voice_records` SET `id` = ?,`title` = ?,`filePath` = ?,`duration` = ?,`fileSize` = ?,`createdAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: VoiceRecordEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.filePath)
        statement.bindLong(4, entity.duration)
        statement.bindLong(5, entity.fileSize)
        val _tmp: String? = __dateTimeConverter.fromLocalDateTime(entity.createdAt)
        if (_tmp == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmp)
        }
        statement.bindLong(7, entity.id)
      }
    }
  }

  public override suspend fun insertVoiceRecord(voiceRecord: VoiceRecordEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfVoiceRecordEntity.insertAndReturnId(_connection,
        voiceRecord)
    _result
  }

  public override suspend fun deleteVoiceRecord(voiceRecord: VoiceRecordEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfVoiceRecordEntity.handle(_connection, voiceRecord)
  }

  public override suspend fun updateVoiceRecord(voiceRecord: VoiceRecordEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfVoiceRecordEntity.handle(_connection, voiceRecord)
  }

  public override fun getAllVoiceRecords(): Flow<List<VoiceRecordEntity>> {
    val _sql: String = "SELECT * FROM voice_records ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("voice_records")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfFilePath: Int = getColumnIndexOrThrow(_stmt, "filePath")
        val _columnIndexOfDuration: Int = getColumnIndexOrThrow(_stmt, "duration")
        val _columnIndexOfFileSize: Int = getColumnIndexOrThrow(_stmt, "fileSize")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<VoiceRecordEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: VoiceRecordEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpFilePath: String
          _tmpFilePath = _stmt.getText(_columnIndexOfFilePath)
          val _tmpDuration: Long
          _tmpDuration = _stmt.getLong(_columnIndexOfDuration)
          val _tmpFileSize: Long
          _tmpFileSize = _stmt.getLong(_columnIndexOfFileSize)
          val _tmpCreatedAt: LocalDateTime
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfCreatedAt)
          }
          val _tmp_1: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_1
          }
          _item =
              VoiceRecordEntity(_tmpId,_tmpTitle,_tmpFilePath,_tmpDuration,_tmpFileSize,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getVoiceRecordById(id: Long): VoiceRecordEntity? {
    val _sql: String = "SELECT * FROM voice_records WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfFilePath: Int = getColumnIndexOrThrow(_stmt, "filePath")
        val _columnIndexOfDuration: Int = getColumnIndexOrThrow(_stmt, "duration")
        val _columnIndexOfFileSize: Int = getColumnIndexOrThrow(_stmt, "fileSize")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: VoiceRecordEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpFilePath: String
          _tmpFilePath = _stmt.getText(_columnIndexOfFilePath)
          val _tmpDuration: Long
          _tmpDuration = _stmt.getLong(_columnIndexOfDuration)
          val _tmpFileSize: Long
          _tmpFileSize = _stmt.getLong(_columnIndexOfFileSize)
          val _tmpCreatedAt: LocalDateTime
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfCreatedAt)
          }
          val _tmp_1: LocalDateTime? = __dateTimeConverter.toLocalDateTime(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_1
          }
          _result =
              VoiceRecordEntity(_tmpId,_tmpTitle,_tmpFilePath,_tmpDuration,_tmpFileSize,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getVoiceRecordCount(): Int {
    val _sql: String = "SELECT COUNT(*) FROM voice_records"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteVoiceRecordById(id: Long) {
    val _sql: String = "DELETE FROM voice_records WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
