import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IApprovalDTO } from '../../../shared/model/approval-dto.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { IManualAttendanceEntry } from '../../../shared/legacy/legacy-model/manual-attendance-entry.model';
import { createRequestOption } from '../../../core/request/request-util';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

type BooleanResponseType = HttpResponse<boolean>;
type EntityArrayResponseType = HttpResponse<IManualAttendanceEntry[]>;

@Injectable({ providedIn: 'root' })
export class AttendanceApprovalService {
  public resourceUrlHr = this.applicationConfigService.getEndpointFor('/api/attendance-mgt/attendance-entry');
  public resourceUrlLM = this.applicationConfigService.getEndpointFor('/api/common/attendance-entry');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getAllPendingHr(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IManualAttendanceEntry[]>(this.resourceUrlHr + '/hr', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  approveAllHr(): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrlHr + '/approve-all-hr', null, { observe: 'response' });
  }

  denyAllHr(): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrlHr + '/reject-all-hr', null, { observe: 'response' });
  }

  approveSelectedHr(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrlHr + '/approve-selected-hr', approvalDTO, { observe: 'response' });
  }

  denySelectedHr(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrlHr + '/reject-selected-hr', approvalDTO, { observe: 'response' });
  }

  getAllPendingLm(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IManualAttendanceEntry[]>(this.resourceUrlLM + '/lm', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  approveAllLm(): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrlLM + '/approve-all-lm', null, { observe: 'response' });
  }

  denyAllLm(): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrlLM + '/reject-all-lm', null, { observe: 'response' });
  }

  approveSelectedLm(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrlLM + '/approve-selected-lm', approvalDTO, { observe: 'response' });
  }

  denySelectedLm(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrlLM + '/reject-selected-lm', approvalDTO, { observe: 'response' });
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((manualAttendanceEntry: IManualAttendanceEntry) => {
        manualAttendanceEntry.date = manualAttendanceEntry.date ? dayjs(manualAttendanceEntry.date) : undefined;
        manualAttendanceEntry.inTime = manualAttendanceEntry.inTime ? dayjs(manualAttendanceEntry.inTime) : undefined;
        manualAttendanceEntry.outTime = manualAttendanceEntry.outTime ? dayjs(manualAttendanceEntry.outTime) : undefined;
      });
    }
    return res;
  }
}
