import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IRoom } from 'app/entities/room/room.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IRoomRequisition {
  id: number;
  status?: Status | null;
  bookingTrn?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  sanctionedAt?: dayjs.Dayjs | null;
  participantList?: string | null;
  rejectedReason?: string | null;
  bookingStartDate?: dayjs.Dayjs | null;
  bookingEndDate?: dayjs.Dayjs | null;
  startTime?: number | null;
  endTime?: number | null;
  title?: string | null;
  agenda?: string | null;
  optionalParticipantList?: string | null;
  isFullDay?: boolean | null;
  createdBy?: Pick<IUser, 'id' | 'login'> | null;
  updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  sanctionedBy?: Pick<IUser, 'id' | 'login'> | null;
  requester?: Pick<IEmployee, 'id'> | null;
  room?: Pick<IRoom, 'id'> | null;
}

export type NewRoomRequisition = Omit<IRoomRequisition, 'id'> & { id: null };
