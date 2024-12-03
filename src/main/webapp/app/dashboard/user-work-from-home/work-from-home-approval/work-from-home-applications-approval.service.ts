import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IApprovalDTO } from '../../../shared/model/approval-dto.model';
import { createRequestOption } from '../../../core/request/request-util';
import { IWorkFromHomeApplication } from '../../../shared/legacy/legacy-model/work-from-home-application.model';
type BooleanResponseType = HttpResponse<boolean>;
import dayjs from 'dayjs/esm'
import { map } from 'rxjs/operators';
type EntityArrayResponseType = HttpResponse<IWorkFromHomeApplication[]>

@Injectable({ providedIn: 'root' })
export class WorkFromHomeApplicationsApprovalsService {
  public resourceUrl = SERVER_API_URL + 'api/employee-mgt/work-from-home-applications';
  public resourceCommonUrl = SERVER_API_URL + 'api/common/work-from-home-applications';

  constructor(protected http: HttpClient) {}

  enableSelectedLM(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(`${this.resourceCommonUrl}/enable-selected`, approvalDTO, { observe: 'response' });
  }

  disableSelectedLM(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(`${this.resourceCommonUrl}/disable-selected`, approvalDTO, { observe: 'response' });
  }

  // get all  pending subordinate employee List for work-from-home admin approval page(LM)
  getPendingSubordinateEmployeeListLM(req?: any): Observable<HttpResponse<IWorkFromHomeApplication[]>> {
    const options = createRequestOption(req);
    return this.http.get<IWorkFromHomeApplication[]>(`${this.resourceCommonUrl}/pending-sub-ordinate-employees`, {
      params: options,
      observe: 'response',
    }).pipe(map(res => this.convertDateArrayFromServer(res)));
  }

  // get all  pending subordinate employee List for work-from-home admin approval page(LM)
  getAllSubordinateEmployeeListLM(req?: any): Observable<HttpResponse<IWorkFromHomeApplication[]>> {
    const options = createRequestOption(req);
    return this.http.get<IWorkFromHomeApplication[]>(`${this.resourceCommonUrl}/all-sub-ordinate-employees`, {
      params: options,
      observe: 'response',
    }).pipe(map(res => this.convertDateArrayFromServer(res)));
  }
  // HR End Points

  /*HR SeRvice APIs*/

  // get all employee List for work-from-home admin approval page(HR)
  getPendingEmployeeListHR(req?: any): Observable<HttpResponse<IWorkFromHomeApplication[]>> {
    const options = createRequestOption(req);
    return this.http.get<IWorkFromHomeApplication[]>(`${this.resourceUrl}/all-pending-work-applications`, {
      params: options,
      observe: 'response',
    }).pipe(map(res => this.convertDateArrayFromServer(res)));
  }

  // get all employee List for work-from-home admin approval page(HR)
  getEmployeeListForHr(req?: any): Observable<HttpResponse<IWorkFromHomeApplication[]>> {
    const options = createRequestOption(req);
    return this.http.get<IWorkFromHomeApplication[]>(`${this.resourceUrl}/all-employees`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertDateArrayFromServer(res)));
  }

  // get all employee List for work-from-home admin approval page(HR)
  getInActiveEmployeeListForHr(req?: any): Observable<HttpResponse<IWorkFromHomeApplication[]>> {
    const options = createRequestOption(req);
    return this.http.get<IWorkFromHomeApplication[]>(`${this.resourceUrl}/all-inactive-employees`, {
      params: options,
      observe: 'response',
    }).pipe(map(res => this.convertDateArrayFromServer(res)));
  }

  // get all employee List for work-from-home admin approval page(HR)
  getActiveEmployeeListForHr(req?: any): Observable<HttpResponse<IWorkFromHomeApplication[]>> {
    const options = createRequestOption(req);
    return this.http.get<IWorkFromHomeApplication[]>(`${this.resourceUrl}/all-active-employees`, {
      params: options,
      observe: 'response',
    }).pipe(map(res => this.convertDateArrayFromServer(res)));
  }

  getTotalActiveEmployeeListCountForHr(): Observable<HttpResponse<number>> {
    return this.http.get<number>(`${this.resourceUrl}/all-active-employees-numbers`, {
      observe: 'response',
    });
  }

  getTotalInActiveEmployeeListCountForHr(): Observable<HttpResponse<number>> {
    return this.http.get<number>(`${this.resourceUrl}/all-inactive-employees-numbers`, {
      observe: 'response',
    });
  }

  enableSelectedHR(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(`${this.resourceUrl}/enable-selected`, approvalDTO, { observe: 'response' });
  }

  disableSelectedHR(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(`${this.resourceUrl}/disable-selected`, approvalDTO, { observe: 'response' });
  }
  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((workFromHomeApplication: IWorkFromHomeApplication) => {
        workFromHomeApplication.startDate = workFromHomeApplication.startDate ? dayjs(workFromHomeApplication.startDate) : undefined;
        workFromHomeApplication.endDate = workFromHomeApplication.endDate ? dayjs(workFromHomeApplication.endDate) : undefined;
      });
    }
    return res;
  }
}
