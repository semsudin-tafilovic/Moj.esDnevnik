package rs.tafilovic.mojesdnevnik.util

import javax.inject.Inject

class LocalStore @Inject constructor(private val prefsHelper: PrefsHelper) {

    private val selectedStudentIdKey = "selectedStudentId"
    private val selectedSchoolIdKey = "selectedSchoolId"

    fun getSelectedStudentId(): Long {
        return prefsHelper.getLong(selectedStudentIdKey)
    }

    fun setSelectedStudentId(studentId: Long) {
        prefsHelper.setLong(selectedStudentIdKey, studentId)
    }

    fun getSelectedSchoolId(): String? {
        return prefsHelper.getString(selectedSchoolIdKey)
    }

    fun setSelectedSchoolId(schoolId: String) {
        prefsHelper.setString(selectedSchoolIdKey, schoolId)
    }
}