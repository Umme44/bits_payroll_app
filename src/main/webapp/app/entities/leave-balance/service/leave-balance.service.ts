import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILeaveBalance, NewLeaveBalance } from '../leave-balance.model';

export type PartialUpdateLeaveBalance = Partial<ILeaveBalance> & Pick<ILeaveBalance, 'id'>;

export type EntityResponseType = HttpResponse<ILeaveBalance>;
export type EntityArrayResponseType = HttpResponse<ILeaveBalance[]>;

@Injectable({ providedIn: 'root' })
export class LeaveBalanceService {
  /*protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leave-balances');*/
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/leave-balances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaveBalance: NewLeaveBalance): Observable<EntityResponseType> {
    return this.http.post<ILeaveBalance>(this.resourceUrl, leaveBalance, { observe: 'response' });
  }

  update(leaveBalance: ILeaveBalance): Observable<EntityResponseType> {
    return this.http.put<ILeaveBalance>(`${this.resourceUrl}`, leaveBalance, {
      observe: 'response',
    });
  }

  partialUpdate(leaveBalance: PartialUpdateLeaveBalance): Observable<EntityResponseType> {
    return this.http.patch<ILeaveBalance>(`${this.resourceUrl}/${this.getLeaveBalanceIdentifier(leaveBalance)}`, leaveBalance, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeaveBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaveBalance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLeaveBalanceIdentifier(leaveBalance: Pick<ILeaveBalance, 'id'>): number {
    return leaveBalance.id;
  }

  compareLeaveBalance(o1: Pick<ILeaveBalance, 'id'> | null, o2: Pick<ILeaveBalance, 'id'> | null): boolean {
    return o1 && o2 ? this.getLeaveBalanceIdentifier(o1) === this.getLeaveBalanceIdentifier(o2) : o1 === o2;
  }

  addLeaveBalanceToCollectionIfMissing<Type extends Pick<ILeaveBalance, 'id'>>(
    leaveBalanceCollection: Type[],
    ...leaveBalancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const leaveBalances: Type[] = leaveBalancesToCheck.filter(isPresent);
    if (leaveBalances.length > 0) {
      const leaveBalanceCollectionIdentifiers = leaveBalanceCollection.map(
        leaveBalanceItem => this.getLeaveBalanceIdentifier(leaveBalanceItem)!
      );
      const leaveBalancesToAdd = leaveBalances.filter(leaveBalanceItem => {
        const leaveBalanceIdentifier = this.getLeaveBalanceIdentifier(leaveBalanceItem);
        if (leaveBalanceCollectionIdentifiers.includes(leaveBalanceIdentifier)) {
          return false;
        }
        leaveBalanceCollectionIdentifiers.push(leaveBalanceIdentifier);
        return true;
      });
      return [...leaveBalancesToAdd, ...leaveBalanceCollection];
    }
    return leaveBalanceCollection;
  }
  filter(nomineeFilter: ILeaveBalance, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.post<ILeaveBalance[]>(this.resourceUrl + '/filter', nomineeFilter, { params: options, observe: 'response' });
  }

}
