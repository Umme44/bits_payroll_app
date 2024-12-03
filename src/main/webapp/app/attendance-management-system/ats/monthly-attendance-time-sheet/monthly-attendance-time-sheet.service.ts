import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IAttendanceTimeSheet } from 'app/shared/model/attendance-time-sheet.model';
import { ITimeRange } from 'app/shared/model/time-range.model';
import { IMonthlyAttendanceTimeSheet } from 'app/shared/model/monthly-attendance-time-sheet.model';
import { IDateRangeDTO } from 'app/shared/model/DateRangeDTO';
import { Filter } from 'app/common/employee-address-book/filter.model';
import { IAttendanceTimeSheetMini } from 'app/shared/model/attendance-time-sheet-mini.model';
import { MonthlyAttendanceTimeSheetList } from 'app/shared/model/monthly-attendance-time-sheet-list.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';
import dayjs from 'dayjs/esm';
import { IAttendanceEntry } from '../../../shared/legacy/legacy-model/attendance-entry.model';
import { DATE_FORMAT } from '../../../config/input.constants';
import { ILeaveApplication } from '../../../shared/legacy/legacy-model/leave-application.model';
import {
  IMonthlyAttendanceTimeSheetEmployeeList
} from '../../../shared/model/monthly-attendance-time-sheet-employee-list.model';

type EntityResponseType = HttpResponse<IMonthlyAttendanceTimeSheet>;
type EntityArrayResponseType = HttpResponse<IMonthlyAttendanceTimeSheet[]>;

@Injectable({ providedIn: 'root' })
export class MonthlyAttendanceTimeSheetService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/attendance-time-sheet-admin/time-ranged-report');
  public resourceUrlForFilteredData = this.applicationConfigService.getEndpointFor(
    'api/attendance-mgt/attendance-time-sheet-admin/filtered-report'
  );
  public resourceUrlAutoLeaveCut = this.applicationConfigService.getEndpointFor('api/attendance-mgt');
  public resourceUrlForMonthlyAtsExport = this.applicationConfigService.getEndpointFor('/api/payroll-mgt/simple-monthly-ats-summary-export');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMonthlyAttendanceTimeSheet[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryUsingFilter(filterObject?: Filter): Observable<EntityArrayResponseType> {
    let copy: Filter = Object.assign({}, filterObject);
    copy.startDate = dayjs(filterObject!.startDate);
    copy.endDate = dayjs(filterObject!.endDate);
    copy = this.convertFilterDateRangeFromClient(copy);
    return this.http
      .post<IMonthlyAttendanceTimeSheet[]>(this.resourceUrlForFilteredData, copy, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  exportMonthlyAtsInExcel(selectedEmployeeAts: IMonthlyAttendanceTimeSheet[]): Observable<Blob> {
    const copyObj = selectedEmployeeAts.map(monthlyAtsList => ({
      pin: monthlyAtsList.pin,
      employeeId: monthlyAtsList.employeeId,
      name: monthlyAtsList.name,
      attendanceTimeSheetMiniList: this.convertDateFromClientForAtsReport(monthlyAtsList.attendanceTimeSheetMiniList!),
    }));

    const reqBodyObj = {
      ...new MonthlyAttendanceTimeSheetList(),
      monthlyAttendanceTimeSheetList: copyObj,
    };

    return this.http.post(this.resourceUrlForMonthlyAtsExport, reqBodyObj, { responseType: 'blob' });
  }

  exportMonthlyAtsInExcelv2(req: IMonthlyAttendanceTimeSheetEmployeeList): Observable<Blob> {
    return this.http.post(this.resourceUrlForMonthlyAtsExport, req, { responseType: 'blob' });
  }

  queryByDateRange(timeRange: IDateRangeDTO): Observable<EntityArrayResponseType> {
    //const copy = this.convertDateRangeClient(timeRange);
    const copy = this.convertDateRangeFromClient(timeRange);
    return this.http
      .post<IMonthlyAttendanceTimeSheet[]>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  autoCutLeave(timeRange: ITimeRange): Observable<HttpResponse<boolean>> {
    const copy = this.convertDateRangeClient(timeRange);
    return this.http.post<boolean>(this.resourceUrlAutoLeaveCut + '/auto-leave-cut', copy, { observe: 'response' });
    // .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  autoCutLeaveSummary(timeRange: ITimeRange): Observable<HttpResponse<ILeaveApplication[]>> {
    const copy = this.convertDateRangeClient(timeRange);
    return this.http.post<ILeaveApplication[]>(this.resourceUrlAutoLeaveCut + '/auto-leave-cut-summary', copy, { observe: 'response' });
    //.pipe(map((res: HttpResponse<ILeaveApplication[]>) => this.convertDateArrayFromServer(res)));
  }

  autoCutLeaveSummaryReport(timeRange: ITimeRange): Observable<Blob> {
    const copy = this.convertDateRangeClient(timeRange);
    return this.http.post(this.resourceUrlAutoLeaveCut + '/auto-leave-cut-summary-report', copy, { responseType: 'blob' });
  }

  protected convertDateFromClient(attendanceEntry: IAttendanceTimeSheet): IAttendanceEntry {
    const copy: IAttendanceTimeSheet = Object.assign({}, attendanceEntry, {
      date: attendanceEntry.date && attendanceEntry.date.isValid() ? attendanceEntry.date.format(DATE_FORMAT) : undefined,
      inTime: attendanceEntry.inTime && attendanceEntry.inTime.isValid() ? attendanceEntry.inTime.toJSON() : undefined,
      outTime: attendanceEntry.outTime && attendanceEntry.outTime.isValid() ? attendanceEntry.outTime.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromClientForAtsReport(monthlyAtsMiniList: IAttendanceTimeSheetMini[]): IAttendanceTimeSheetMini[] {
    const copy: IAttendanceTimeSheetMini[] = [];

    for (let i = 0; i < monthlyAtsMiniList.length; i++) {
      copy[i] = Object.assign({}, monthlyAtsMiniList[i], {
        date:
          monthlyAtsMiniList[i].date && monthlyAtsMiniList[i].date!.isValid() ? monthlyAtsMiniList[i].date!.format(DATE_FORMAT) : undefined,
      });
    }
    return copy;
  }

  protected convertDateRangeClient(timeRange: ITimeRange): ITimeRange {
    const copy: ITimeRange = Object.assign({}, timeRange, {
      startDate: timeRange.startDate,
      endDate: timeRange.endDate,
    });
    return copy;
  }

  protected convertFilterDateRangeFromClient(filter: Filter): Filter {
    const copy: Filter = Object.assign({}, filter, {
      startDate: filter.startDate && dayjs(filter.startDate).isValid() ? dayjs(filter.startDate).format(DATE_FORMAT) : undefined,
      endDate: filter.endDate && dayjs(filter.endDate).isValid() ? dayjs(filter.endDate).format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateRangeFromClient(dateRangeDTO: IDateRangeDTO): IDateRangeDTO {
    const copy: IDateRangeDTO = Object.assign({}, dateRangeDTO, {
      startDate:
        dateRangeDTO.startDate && dayjs(dateRangeDTO.startDate).isValid() ? dayjs(dateRangeDTO.startDate).format(DATE_FORMAT) : undefined,
      endDate: dateRangeDTO.endDate && dayjs(dateRangeDTO.endDate).isValid() ? dayjs(dateRangeDTO.endDate).format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      if (res.body.attendanceTimeSheetMiniList) {
        for (let i = 0; i < res.body.attendanceTimeSheetMiniList.length; i++) {
          res.body.attendanceTimeSheetMiniList[i].date = res.body.attendanceTimeSheetMiniList[i].date
            ? dayjs(res.body.attendanceTimeSheetMiniList[i].date)
            : undefined;
        }
      }
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    const response = res.body;
    if (response) {
      for (let m = 0; m < response.length; m++) {
        if (response[m].attendanceTimeSheetMiniList) {
          for (let i = 0; i < response[m].attendanceTimeSheetMiniList!.length; i++) {
            response[m].attendanceTimeSheetMiniList![i].date = response[m].attendanceTimeSheetMiniList![i].date
              ? dayjs(response[m].attendanceTimeSheetMiniList![i].date)
              : undefined;
          }
        }
      }
    }
    return res;
  }
}
