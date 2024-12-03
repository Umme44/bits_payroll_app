import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IApprovalDTO } from '../../../shared/model/approval-dto.model';
import { INominee } from '../nominee.model';
import { IPfNominee } from '../pf-nominee.model';
import { createRequestOption } from '../../../core/request/request-util';

type EntityArrayResponseType2 = HttpResponse<INominee[]>;
type EntityArrayResponseType = HttpResponse<IPfNominee[]>;

@Injectable({ providedIn: 'root' })
export class GeneralGfNomineeApprovalService {
  public resourceUrl_V2 = SERVER_API_URL + 'api/pf-mgt/general-nominees-approval';
  public resourceUrl_V3 = SERVER_API_URL + 'api/pf-mgt/gf-nominees-approval';

  constructor(protected http: HttpClient) {}

  getAllApprovedOrPendingNomineeList(req?: any): Observable<EntityArrayResponseType2> {
    const options = createRequestOption(req);
    return this.http.get<INominee[]>(this.resourceUrl_V3 + '/all-pending-approved-nominee-list', { params: options, observe: 'response' });
  }

  getPendingList(): Observable<EntityArrayResponseType2> {
    return this.http.get<INominee[]>(this.resourceUrl_V2 + '/pending-list', { observe: 'response' });
  }

  getApprovedList(): Observable<EntityArrayResponseType2> {
    return this.http.get<INominee[]>(this.resourceUrl_V2 + '/approved-list', { observe: 'response' });
  }

  approveSelected(approvalDto: IApprovalDTO): Observable<HttpResponse<boolean>> {
    return this.http.post<boolean>(this.resourceUrl_V2 + '/approve', approvalDto, { observe: 'response' });
  }

  rejectSelected(approvalDto: IApprovalDTO): Observable<HttpResponse<boolean>> {
    return this.http.post<boolean>(this.resourceUrl_V2 + '/reject', approvalDto, { observe: 'response' });
  }
}
