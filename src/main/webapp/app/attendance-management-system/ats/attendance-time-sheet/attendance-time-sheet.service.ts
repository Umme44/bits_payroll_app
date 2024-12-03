import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IAttendanceTimeSheet } from 'app/shared/model/attendance-time-sheet.model';
import { ITimeRange } from 'app/shared/model/time-range.model';
import { IDateRangeDTO } from 'app/shared/model/DateRangeDTO';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { IAttendanceEntry } from '../../../shared/legacy/legacy-model/attendance-entry.model';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from '../../../config/input.constants';
import { createRequestOption } from '../../../core/request/request-util';

type EntityResponseType = HttpResponse<IAttendanceEntry>;
type EntityArrayResponseType = HttpResponse<IAttendanceEntry[]>;

export const HAS_PENDING_MANUAL_ATTENDANCE_MSG = 'You have Attendance Application during this date';
export const HAS_PENDING_MOVEMENT_ENTRY_MSG = 'You have pending Movement Entry during this date';
export const HAS_PENDING_LEAVE_APPLICATION_MSG = 'You have Leave Application during this date';
export const HAS_PRESENT_STATUS_MSG = 'You have Attendance during this date';
export const HAS_MOVEMENT_ENTRY_MSG = 'You have Movement Entry during this date';
export const HAS_APPROVED_LEAVE_MSG = 'You have approved Leave during this date';

export const HAS_PENDING_MANUAL_ATTENDANCE_MSG_ADMIN = 'Selected employee has Attendance Application during this date';
export const HAS_PENDING_MOVEMENT_ENTRY_MSG_ADMIN = 'Selected employee has pending Movement Entry during this date';
export const HAS_PENDING_LEAVE_APPLICATION_MSG_ADMIN = 'Selected employee has Leave Application during this date';
export const HAS_PRESENT_STATUS_MSG_ADMIN = 'Selected employee has Attendance during this date';
export const HAS_MOVEMENT_ENTRY_MSG_ADMIN = 'Selected employee has Movement Entry during this date';
export const HAS_APPROVED_LEAVE_MSG_ADMIN = 'Selected employee has approved Leave during this date';

@Injectable({ providedIn: 'root' })
export class AttendanceTimeSheetService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-user/attendance-time-sheet');
  public resourceUrlForExportXL = this.applicationConfigService.getEndpointFor('api/attendance-user/export-attendance-time-sheet');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAttendanceTimeSheet[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  dashboardUserAttendance(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IAttendanceTimeSheet[]>(this.resourceUrl + '/dashboard-history', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryByDateRange(dateRange: IDateRangeDTO): Observable<EntityArrayResponseType> {
    const copy = this.convertDateRangeFromClient(dateRange);
    return this.http
      .post<IAttendanceTimeSheet[]>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findApplicationsByDateRange(dateRange: IDateRangeDTO): Observable<EntityResponseType> {
    const copy = this.convertDateRangeFromClient(dateRange);
    return this.http
      .post<IAttendanceTimeSheet>(this.resourceUrl + '/pending-applications-between-date-range', copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  exportAtsDataInXL(dateRange: IDateRangeDTO): Observable<Blob> {
    const copy = this.convertDateRangeFromClient(dateRange);
    return this.http.post(this.resourceUrlForExportXL, copy, { responseType: 'blob' });
  }

  protected convertDateRangeFromClient(dateRangeDTO: IDateRangeDTO): IDateRangeDTO {
    const copy: IDateRangeDTO = Object.assign({}, dateRangeDTO, {
      startDate:
        dateRangeDTO.startDate && dayjs(dateRangeDTO.startDate).isValid() ? dayjs(dateRangeDTO.startDate).format(DATE_FORMAT) : undefined,
      endDate: dateRangeDTO.endDate && dayjs(dateRangeDTO.endDate).isValid() ? dayjs(dateRangeDTO.endDate).format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromClient(attendanceEntry: IAttendanceTimeSheet): IAttendanceEntry {
    const copy: IAttendanceTimeSheet = Object.assign({}, attendanceEntry, {
      date: attendanceEntry.date && attendanceEntry.date.isValid() ? attendanceEntry.date.format(DATE_FORMAT) : undefined,
      inTime: attendanceEntry.inTime && attendanceEntry.inTime.isValid() ? attendanceEntry.inTime.toJSON() : undefined,
      outTime: attendanceEntry.outTime && attendanceEntry.outTime.isValid() ? attendanceEntry.outTime.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateRangeClient(timeRange: ITimeRange): ITimeRange {
    const copy: ITimeRange = Object.assign({}, timeRange, {
      startDate: timeRange.startDate,
      endDate: timeRange.endDate,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.inTime = res.body.inTime ? dayjs(res.body.inTime) : undefined;
      res.body.outTime = res.body.outTime ? dayjs(res.body.outTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((attendanceEntry: IAttendanceTimeSheet) => {
        attendanceEntry.date = attendanceEntry.date ? dayjs(attendanceEntry.date) : undefined;
        attendanceEntry.inTime = attendanceEntry.inTime ? dayjs(attendanceEntry.inTime) : undefined;
        attendanceEntry.outTime = attendanceEntry.outTime ? dayjs(attendanceEntry.outTime) : undefined;
      });
    }
    return res;
  }

  protected getCurrentDaysAttendanceOfCurrentEmployee(): Observable<HttpResponse<IAttendanceTimeSheet>> {
    return this.http.get<IAttendanceTimeSheet>(`${this.resourceUrl}/isExist`, { observe: 'response' });
  }
}
