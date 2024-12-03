import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILeaveAllocation, NewLeaveAllocation } from '../leave-allocation.model';

export type PartialUpdateLeaveAllocation = Partial<ILeaveAllocation> & Pick<ILeaveAllocation, 'id'>;

export type EntityResponseType = HttpResponse<ILeaveAllocation>;
export type EntityArrayResponseType = HttpResponse<ILeaveAllocation[]>;

@Injectable({ providedIn: 'root' })
export class LeaveAllocationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/leave-allocations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaveAllocation: NewLeaveAllocation): Observable<EntityResponseType> {
    return this.http.post<ILeaveAllocation>(this.resourceUrl, leaveAllocation, { observe: 'response' });
  }

  update(leaveAllocation: ILeaveAllocation): Observable<EntityResponseType> {
    return this.http.put<ILeaveAllocation>(`${this.resourceUrl}`, leaveAllocation, {
      observe: 'response',
    });
  }

  partialUpdate(leaveAllocation: PartialUpdateLeaveAllocation): Observable<EntityResponseType> {
    return this.http.patch<ILeaveAllocation>(`${this.resourceUrl}/${this.getLeaveAllocationIdentifier(leaveAllocation)}`, leaveAllocation, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeaveAllocation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaveAllocation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLeaveAllocationIdentifier(leaveAllocation: Pick<ILeaveAllocation, 'id'>): number {
    return leaveAllocation.id;
  }

  compareLeaveAllocation(o1: Pick<ILeaveAllocation, 'id'> | null, o2: Pick<ILeaveAllocation, 'id'> | null): boolean {
    return o1 && o2 ? this.getLeaveAllocationIdentifier(o1) === this.getLeaveAllocationIdentifier(o2) : o1 === o2;
  }

  addLeaveAllocationToCollectionIfMissing<Type extends Pick<ILeaveAllocation, 'id'>>(
    leaveAllocationCollection: Type[],
    ...leaveAllocationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const leaveAllocations: Type[] = leaveAllocationsToCheck.filter(isPresent);
    if (leaveAllocations.length > 0) {
      const leaveAllocationCollectionIdentifiers = leaveAllocationCollection.map(
        leaveAllocationItem => this.getLeaveAllocationIdentifier(leaveAllocationItem)!
      );
      const leaveAllocationsToAdd = leaveAllocations.filter(leaveAllocationItem => {
        const leaveAllocationIdentifier = this.getLeaveAllocationIdentifier(leaveAllocationItem);
        if (leaveAllocationCollectionIdentifiers.includes(leaveAllocationIdentifier)) {
          return false;
        }
        leaveAllocationCollectionIdentifiers.push(leaveAllocationIdentifier);
        return true;
      });
      return [...leaveAllocationsToAdd, ...leaveAllocationCollection];
    }
    return leaveAllocationCollection;
  }
}
