import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IApprovalDTO } from '../../../shared/model/approval-dto.model';
import { IPfNominee } from '../pf-nominee.model';

type EntityArrayResponseType = HttpResponse<IPfNominee[]>;

@Injectable({ providedIn: 'root' })
export class PfNomineeApprovalService {
  public resourceUrl = SERVER_API_URL + 'api/pf-mgt/pf-nominees-approval';

  constructor(protected http: HttpClient) {}

  getPendingList(): Observable<EntityArrayResponseType> {
    return this.http.get<IPfNominee[]>(this.resourceUrl + '/pending-list', { observe: 'response' });
  }

  getApprovedList(): Observable<EntityArrayResponseType> {
    return this.http.get<IPfNominee[]>(this.resourceUrl + '/approved-list', { observe: 'response' });
  }

  approveSelected(approvalDto: IApprovalDTO): Observable<HttpResponse<boolean>> {
    return this.http.post<boolean>(this.resourceUrl + '/approve', approvalDto, { observe: 'response' });
  }

  rejectSelected(approvalDto: IApprovalDTO): Observable<HttpResponse<boolean>> {
    return this.http.post<boolean>(this.resourceUrl + '/reject', approvalDto, { observe: 'response' });
  }
}
