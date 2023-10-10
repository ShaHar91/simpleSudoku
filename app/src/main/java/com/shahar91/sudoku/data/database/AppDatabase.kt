package com.shahar91.sudoku.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shahar91.sudoku.data.database.dao.SudokuDao
import com.shahar91.sudoku.data.database.dao.SudokuGameDao
import com.shahar91.sudoku.data.database.entity.Sudoku
import com.shahar91.sudoku.data.database.entity.SudokuGame
import com.shahar91.sudoku.data.database.enums.Difficulty
import com.shahar91.sudoku.data.database.utils.Converters
import com.shahar91.sudoku.data.database.utils.DBConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(
    entities = [
        Sudoku::class,
        SudokuGame::class
    ], version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sudokuDao(): SudokuDao
    abstract fun sudokuGameDao(): SudokuGameDao

    companion object {
        private val mScope = CoroutineScope(SupervisorJob())

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context, AppDatabase::class.java, DBConstants.DATABASE_NAME)
                    .addCallback(SudokuDatabaseCallback(mScope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class SudokuDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
                scope.launch {
                    val sudokuGameDao = database.sudokuGameDao().also { it.deleteAllFromTable() }
                    val sudokuDao = database.sudokuDao().also { it.deleteAllFromTable() }

                    val sudokuGameList = listOf(
                        SudokuGame(0, "360000000004230800000004200070460003820000014500013020001900000007048300000000045", Difficulty.EASY),
                        SudokuGame(1, "510003000900020050004950800000309260002010900089602000003071400020090005000500027", Difficulty.EASY),
                        SudokuGame(2, "200050000000007409401008073057003000100000007000700640710900806603100000000030001", Difficulty.EASY),
                        SudokuGame(3, "000850060070000000006070328103000980040703010095000402459080600000000050030065000", Difficulty.EASY),
                        SudokuGame(4, "037098000060200005001400300000040030400030007080070000005001800900004060000650710", Difficulty.EASY),
                        SudokuGame(5, "400070060008106097070000038004700000092050870000008500920000010340601700060020003", Difficulty.EASY),
                        SudokuGame(6, "080310007001004000760092300100040830000000000048050002002570086000400500800039040", Difficulty.EASY),
                        SudokuGame(7, "015000006803004005040080903000009010008306200070400000201040090600700801400000630", Difficulty.EASY),
                        SudokuGame(8, "005804206008016009060009000040070005012000970500020080000100050400590300601302700", Difficulty.EASY),
                        SudokuGame(9, "005700041020000300800306079531200000000000000000001738980405002003000090240009800", Difficulty.EASY),
                        SudokuGame(10, "072003090040629000000070050800104760000000000061807005090040000000368020030900840", Difficulty.EASY),
                        SudokuGame(11, "700002080010907000089006021007000060000218000050000400530800290000601050060500008", Difficulty.EASY),
                        SudokuGame(12, "400000100500007000006148002004201730308050204072304600200463800000900001005000009", Difficulty.EASY),
                        SudokuGame(13, "000047320172000009003600000001324060200000005060579400000003200500000893034250000", Difficulty.EASY),
                        SudokuGame(14, "090540000047800000000000089004000010503406908070000600380000000000003190000015020", Difficulty.EASY),
                        SudokuGame(15, "900008000007069000068000510800000627600080001312000008059000360000930700000200004", Difficulty.EASY),
                        SudokuGame(16, "000309007004001083100400960001006050090000040050800300019008004460200800500907000", Difficulty.EASY),
                        SudokuGame(17, "170380200052007000000006070400000060093000840010000009020800000000600320008024095", Difficulty.EASY),
                        SudokuGame(18, "002650030937000000000003402000094070003702100090310000109200000000000754070036900", Difficulty.EASY),
                        SudokuGame(19, "025000890040510020000009500009060005210000084400080700002100000070048050084000970", Difficulty.EASY),
                        SudokuGame(20, "650000070000506000014000005007009000002314700000700800500000630000201000030000097", Difficulty.MEDIUM),
                        SudokuGame(21, "007009000002314700000700800500000630000201000030000097650000070000506000014000005", Difficulty.MEDIUM),
                        SudokuGame(22, "010000280000040301930870000300002000205307908000500002000085097107030000089000010", Difficulty.MEDIUM),
                        SudokuGame(23, "000002000002850600700340050000508001056010320300609000080094003007063400000100000", Difficulty.MEDIUM),
                        SudokuGame(24, "608900000000000604000020570000507300800040009006103000042030000103000000000009402", Difficulty.MEDIUM),
                        SudokuGame(25, "308500040070001009009040700034005007001000900500700320007050600900100070040006801", Difficulty.MEDIUM),
                        SudokuGame(26, "020900080003060900950400000000000607091070830702000000000006018005090700060008040", Difficulty.MEDIUM),
                        SudokuGame(27, "000706004040000600000050009109800076504607902670005103400020000003000020700504000", Difficulty.MEDIUM),
                        SudokuGame(28, "100350700040072000007108000030500201079000650601004030000401500000720080008035006", Difficulty.MEDIUM),
                        SudokuGame(29, "000610000047050800012000000000490700405000908008032000000000230004020180000064000", Difficulty.MEDIUM),
                        SudokuGame(30, "007900000120000906900001027500790010000080000090012008480200005702000049000005200", Difficulty.MEDIUM),
                        SudokuGame(31, "000006080034800016000020345205000000000682000000000409748030000610004530050200000", Difficulty.MEDIUM),
                        SudokuGame(32, "000009203000703006001060005008070004020105070700020900500080400400307000302500000", Difficulty.MEDIUM),
                        SudokuGame(33, "000000008700400050125600300200314005000902000300865009002003471010006002800000000", Difficulty.MEDIUM),
                        SudokuGame(34, "001700050730000690090001000080200100079030580004006030000300020026000015040005800", Difficulty.MEDIUM),
                        SudokuGame(35, "903570102040000000001029005300600090000010000010002003200940300000000050409083206", Difficulty.MEDIUM),
                        SudokuGame(36, "000005621004000375002000000020380007080010030700056090000000100143000700698500000", Difficulty.MEDIUM),
                        SudokuGame(37, "209000600000000001000001940040019000032506470000430050073800000800000000006000207", Difficulty.MEDIUM),
                        SudokuGame(38, "000800079500900002984000300000075000360000051000630000005000823800009006610002000", Difficulty.MEDIUM),
                        SudokuGame(39, "000970016000560300000001002070080605005103400804050030200600000007035000310097000", Difficulty.MEDIUM),
                        SudokuGame(40, "009000000080605020501078000000000700706040102004000000000720903090301080000000600", Difficulty.HARD),
                        SudokuGame(41, "360000000004230800000004200070460003820000014500013020001900000007048300000000045", Difficulty.HARD),
                        SudokuGame(42, "200060000040000700060705000385402600600000002002607513000804050009000020000090001", Difficulty.HARD),
                        SudokuGame(43, "098100003006000270300500000100006000870090015000700009000005007087000300600003120", Difficulty.HARD),
                        SudokuGame(44, "900040200000790140000100700370050020000000000040060083009008000084075000005030002", Difficulty.HARD),
                        SudokuGame(45, "800000047016070000000009600207580006080604070100097804002300000000060720650000008", Difficulty.HARD),
                        SudokuGame(46, "009020000400010000006908704600040050100309008040070002905403200000090003000080500", Difficulty.HARD),
                        SudokuGame(47, "400000300030450600020038040970804000600000004000905061060240030004087090003000008", Difficulty.HARD),
                        SudokuGame(48, "003002004100000092000800030426008001000050000800300427060003000340000008200900700", Difficulty.HARD),
                        SudokuGame(49, "901027500047600000000900400000870020003000100070016000005009000000005390002740605", Difficulty.HARD),
                        SudokuGame(50, "200009740600502000850070000008600000705000603000001900000090072000705009097200008", Difficulty.HARD),
                        SudokuGame(51, "798000000000308005002040000000002014060984020420700000000070100100803000000000536", Difficulty.HARD),
                        SudokuGame(52, "200001060000400000090068004036090200120000095005030170400720050000004000070500002", Difficulty.HARD),
                        SudokuGame(53, "003006078000000205280400000006004009040103060800900700000002057107000000630700900", Difficulty.HARD),
                        SudokuGame(54, "000007892009004700085002000000000405001605900508000000000200610002100500417500000", Difficulty.HARD),
                        SudokuGame(55, "000320500000900310094000006000005400480000059002600000600000840057003000001067000", Difficulty.HARD),
                        SudokuGame(56, "054080000000307501000005090080004900709000204003900070090400000301502000000090450", Difficulty.HARD),
                        SudokuGame(57, "000130800000002070000089040500300100430000052002001006050940000060700000009068000", Difficulty.HARD),
                        SudokuGame(58, "000400080040000690300680704600700000100050007000002008201043005074000030050009000", Difficulty.HARD),
                        SudokuGame(59, "053019700000000000000070609090027003030604090500390080106030000000000000002960810", Difficulty.HARD)
                    ).also { sudokuGameDao.insertMany(it) }

                    sudokuGameList.map { Sudoku(it.id + 10, 0, 0, it.id) }.also { sudokuDao.insertMany(it) }
                }
            }
        }
    }
}