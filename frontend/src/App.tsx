import { Routes, Route, Navigate } from "react-router-dom";
import { Navigation } from "@/components/layout/Navigation";
import Transactions from "./pages/Transactions";
import Budgets from "./pages/Budgets";
import Settings from "./pages/Settings";
import NotFound from "./pages/NotFound";
import DashboardPage from "./pages/dashboard-page";
import { useAuthStore } from "./store/auth-store";
import { useProfile } from "./hooks/use-profile";
import { useEffect } from "react";
import LoginPage from "./pages/login-page";

const App = () => {
	const { setUser } = useAuthStore();
	// const { data, isSuccess, isError, isLoading } = useProfile();

	// useEffect(() => {
	// 	if (isSuccess && data) {
	// 		setUser(data);
	// 	}
	// }, [isSuccess, data, setUser]);

	// if (isLoading) {
	// 	return <div>Loading...</div>;
	// }

	// if (isError) {
	// 	return <Navigate to='/login' />;
	// }

	return (
		<div className='min-h-screen bg-background'>
			<Navigation />
			<main className='container mx-auto px-4 py-8'>
				<Routes>
					<Route path='/' element={<DashboardPage />} />
					<Route path='/transactions' element={<Transactions />} />
					<Route path='/budgets' element={<Budgets />} />
					<Route path='/settings' element={<Settings />} />
					<Route path='/login' element={<LoginPage />} />
					<Route path='*' element={<NotFound />} />
				</Routes>
			</main>
		</div>
	);
};

export default App;
