import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IAttendanceTimeSheet } from '../../../shared/model/attendance-time-sheet.model';
import { ITimeRangeAndEmployeeId } from './time-range-and-employeeId.model';
import { IEmployeeMinimal } from 'app/shared/model/employee-minimal.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import dayjs from 'dayjs/esm';
import { IAttendanceEntry } from '../../../shared/legacy/legacy-model/attendance-entry.model';
import { DATE_FORMAT } from '../../../config/input.constants';

type EntityResponseType = HttpResponse<IAttendanceTimeSheet>;
type EntityArrayResponseType = HttpResponse<IAttendanceTimeSheet[]>;

@Injectable({ providedIn: 'root' })
export class AttendanceTimeSheetAdminService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('/api/attendance-mgt/attendance-time-sheet-admin');
  public resourceUrlForExportXL = this.applicationConfigService.getEndpointFor('/api/attendance-mgt/export-attendance-time-sheet');
  public resourceUrlForMyTeamAtsExportXL = this.applicationConfigService.getEndpointFor('/api/common/export-my-team-ats');
  public resourceUrlCommonForMyTeam = this.applicationConfigService.getEndpointFor('api/common/my-team-member/attendance-time-sheet');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  queryByDateRange(timeRangeAndEmployeeId: ITimeRangeAndEmployeeId): Observable<EntityArrayResponseType> {
    const copy = this.convertDateRangeClient(timeRangeAndEmployeeId);
    return this.http
      .post<IAttendanceTimeSheet[]>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryByDateRangeForTeamMember(timeRangeAndEmployeeId: ITimeRangeAndEmployeeId): Observable<EntityArrayResponseType> {
    const copy = this.convertDateRangeClient(timeRangeAndEmployeeId);
    return this.http
      .post<IAttendanceTimeSheet[]>(this.resourceUrlCommonForMyTeam, copy, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  exportAtsDataInXL(dateRange: ITimeRangeAndEmployeeId): Observable<Blob> {
    const copy = this.convertDateRangeClient(dateRange);
    return this.http.post(this.resourceUrlForExportXL, copy, { responseType: 'blob' });
  }

  exportMyTeamAtsDataInXL(dateRange: ITimeRangeAndEmployeeId): Observable<Blob> {
    const copy = this.convertDateRangeClient(dateRange);
    return this.http.post(this.resourceUrlForMyTeamAtsExportXL, copy, { responseType: 'blob' });
  }

  protected convertDateFromClient(attendanceEntry: IAttendanceTimeSheet): IAttendanceEntry {
    const copy: IAttendanceTimeSheet = Object.assign({}, attendanceEntry, {
      date: attendanceEntry.date && attendanceEntry.date.isValid() ? attendanceEntry.date.format(DATE_FORMAT) : undefined,
      inTime: attendanceEntry.inTime && attendanceEntry.inTime.isValid() ? attendanceEntry.inTime.toJSON() : undefined,
      outTime: attendanceEntry.outTime && attendanceEntry.outTime.isValid() ? attendanceEntry.outTime.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateRangeClient(timeRangeAndEmployeeId: ITimeRangeAndEmployeeId): ITimeRangeAndEmployeeId {
    const copy: ITimeRangeAndEmployeeId = Object.assign({}, timeRangeAndEmployeeId, {
      employeeId: timeRangeAndEmployeeId.employeeId,
      startDate: timeRangeAndEmployeeId.startDate,
      endDate: timeRangeAndEmployeeId.endDate,
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
      res.body.forEach((attendanceTimeSheet: IAttendanceTimeSheet) => {
        attendanceTimeSheet.date = attendanceTimeSheet.date ? dayjs(attendanceTimeSheet.date) : undefined;
        attendanceTimeSheet.inTime = attendanceTimeSheet.inTime ? dayjs(attendanceTimeSheet.inTime) : undefined;
        attendanceTimeSheet.outTime = attendanceTimeSheet.outTime ? dayjs(attendanceTimeSheet.outTime) : undefined;
      });
    }
    return res;
  }

  protected getCurrentDaysAttendanceOfCurrentEmployee(): Observable<HttpResponse<IAttendanceTimeSheet>> {
    return this.http.get<IAttendanceTimeSheet>(`${this.resourceUrl}/isExist`, { observe: 'response' });
  }

  public getEmployeeById(id: number): Observable<HttpResponse<IEmployeeMinimal>> {
    return this.http.get<IEmployeeMinimal>(`${this.resourceUrlCommonForMyTeam}/${id}`, { observe: 'response' });
  }
}
